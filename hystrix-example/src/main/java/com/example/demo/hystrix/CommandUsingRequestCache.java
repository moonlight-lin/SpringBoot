package com.example.demo.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;


public class CommandUsingRequestCache extends HystrixCommand<Boolean> {

    private final int value;

    public CommandUsingRequestCache(int value) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.value = value;
    }

    @Override
    protected Boolean run() {
        return value == 0 || value % 2 == 0;
    }

    @Override
    protected String getCacheKey() {
    	// 获取 key，相同的 key 使用同一个 cache，这里直接把传给 HystrixCommand 的 value 作为 key
        return String.valueOf(value);
    }
}
