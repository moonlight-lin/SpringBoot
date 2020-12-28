package com.example.demo.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.hystrix.CommandCollapserGetValueForKey;
import com.example.demo.hystrix.CommandHelloFailure;
import com.example.demo.hystrix.CommandHelloWorld;
import com.example.demo.hystrix.CommandUsingRequestCache;
import com.example.demo.hystrix.CommandWithProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import rx.Observable;

@RestController
public class Controller {
    @RequestMapping("/hello")
    public String hello() throws InterruptedException, ExecutionException {
        // 就算同样的命令也要重新 new 一个
        
        // sync
        // 实际上调用的是 queue().get()
        String exeucte_result = new CommandHelloWorld("Bob").execute();
        
        // async
        // 实际调用的是 toObservable().toBlocking().toFuture()
        // 当执行 future.get() 时才真正执行命令
        Future<String> future = new CommandHelloWorld("World").queue();
        
        // async
        // returns a “hot” Observable that executes the command immediately, 
        // though because the Observable is filtered through a ReplaySubject you are not in danger
        // of losing any items that it emits before you have a chance to subscribe
        //
        // 实际上是调用了 toObservable().subscribe 让命令执行，再调用 doOnUnsubscribe 好让后面的用户代码执行 subscribe
        final StringBuilder observe_string = new StringBuilder();
        Observable<String> observe = new CommandHelloWorld("Hystirx").observe();
        //observe.subscribe(d -> System.out.println(d));
        observe.subscribe(d -> observe_string.append(d));
        
        // async
        // returns a “cold” Observable that won’t execute the command and begin emitting its results 
        // until you subscribe to the Observable
        final StringBuilder observable_string = new StringBuilder();
        Observable<String> observable = new CommandHelloWorld("Spring").toObservable();
        //observable.subscribe(d -> System.out.println(d));
        observable.subscribe(d -> observable_string.append(d));
        
        // 无论是 observe() 还是 toObservable() 实际都是异步的，因为 subscribe 函数不会阻塞，
        // 可以看到 observe_string 和 observable_string 有时有值，有时是空的
        return "execute: " + exeucte_result + "<br>queue: " + future.get() + 
               "<br>observe: " + observe_string + "<br>observable: " + observable_string;
    }
    
    @RequestMapping("/fail")
    public String fail() {
        return new CommandHelloFailure("Hystirx").execute();
    }
    
    @RequestMapping("/cache")
    public String cache() {
        final StringBuilder result = new StringBuilder();
        
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            CommandUsingRequestCache command2a = new CommandUsingRequestCache(2);
            CommandUsingRequestCache command2b = new CommandUsingRequestCache(2);

            result.append("<br>first: " + command2a.execute());
            result.append("<br>from cache: " + command2a.isResponseFromCache());

            result.append("<br>second: " + command2b.execute());
            result.append("<br>from cache: " + command2b.isResponseFromCache());
        } finally {
            context.shutdown();
        }

        // start a new request context
        context = HystrixRequestContext.initializeContext();
        try {
            CommandUsingRequestCache command3b = new CommandUsingRequestCache(2);
            result.append("<br>third: " + command3b.execute());
            result.append("<br>from cache: " + command3b.isResponseFromCache());
        } finally {
            context.shutdown();
        }
        
        return result.toString();
    }
    
    @RequestMapping("/collapser")
    public String collapser() throws InterruptedException, ExecutionException {
        final StringBuilder result = new StringBuilder();
        
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        
        Future<String> f1 = new CommandCollapserGetValueForKey(1).queue();
        Future<String> f2 = new CommandCollapserGetValueForKey(2).queue();
        Future<String> f3 = new CommandCollapserGetValueForKey(3).queue();
        Future<String> f4 = new CommandCollapserGetValueForKey(4).queue();

        try {
            System.out.println("f1.get()");
            result.append("<br>" + f1.get());
            System.out.println("f2.get()");
            result.append("<br>" + f2.get());
            System.out.println("f3.get()");
            result.append("<br>" + f3.get());
            System.out.println("f4.get()");
            result.append("<br>" + f4.get());
        } finally {
            context.shutdown();
        }
        
        return result.toString();
    }
    
    @RequestMapping("/properties")
    public String properties() throws InterruptedException, ExecutionException {
        final StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < 20; i++) {
            // i 小于 10 的时候会失败
            // 至少有 10 个调用就开始计算失败率，发现失败率大于 50% 就开启熔断保护
            // 当 i 大于等于 10 的时候本来应该不失败，但由于熔断保护，会直接进入 fall back
            // 熔断状态超过 5 秒后会进入半打开状态
            
            CommandWithProperties command = new CommandWithProperties(i);
            Integer r = command.execute();
            result.append("<br>execute " + i + ", result " + r + ", isCircuitBreakerOpen " + command.isCircuitBreakerOpen());
            
            if (i == 15) {
                // 等待 5s，使得熔断器进入半打开状态
                Thread.sleep(5000);
            }
        }
        
        return result.toString();
    }
}
