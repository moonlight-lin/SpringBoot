package com.example.demo.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class CommandWithProperties extends HystrixCommand<Integer> {
    private Integer id;
 
    public CommandWithProperties(Integer id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueForKey"))
                    .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                           .withCircuitBreakerEnabled(true)     // 打开熔断功能（默认就是 true）
                           .withMetricsRollingStatisticalWindowInMilliseconds(15000)   // 设置统计窗口为 15 秒，默认是 10 秒
                           .withExecutionTimeoutInMilliseconds(900)                    // 命令超时时间是 0.9 秒，默认是 1 秒
                           .withCircuitBreakerRequestVolumeThreshold(10)               // 15 秒统计窗口内至少有 10 个请求，熔断器才进行错误率的计算，默认是 20
                           .withCircuitBreakerSleepWindowInMilliseconds(5000)          // 熔断 5 秒后会重新尝试
                           .withCircuitBreakerErrorThresholdPercentage(50)             // 错误率达到 50 开启熔断保护
                           .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)   // 命令需要获取信号量还是线程，默认是线程
                           .withExecutionIsolationSemaphoreMaxConcurrentRequests(10)));  // 最大并发请求量
        
        this.id = id;
    }
 
    @Override
    protected Integer run() {
        if (this.id < 10) {
            try {
                // 迫使 timeout
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return this.id;
    }
 
    @Override
    protected Integer getFallback() {
        return -1;
    }
}
