package com.example.dubbo.provider.impl;

import com.example.dubbo.interfaces.GreetingService;


public class GreetingServiceImpl implements GreetingService {

    @Override
    public String sayHi(String name) {
        System.out.println("receive msg " + name);
        return "hi, " + name;
    }

}
