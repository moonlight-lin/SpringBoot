package com.example.dubbo.provider;

import java.util.concurrent.CountDownLatch;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import com.example.dubbo.interfaces.GreetingService;
import com.example.dubbo.provider.impl.GreetingServiceImpl;


public class Provider {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "localhost");

    public static void main(String[] args) throws Exception {
        ServiceConfig<GreetingService> service = new ServiceConfig<>();
        service.setApplication(new ApplicationConfig("first-dubbo-provider"));
        service.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        service.setInterface(GreetingService.class);
        service.setRef(new GreetingServiceImpl());
        service.export();

        System.out.println("dubbo service started");
        new CountDownLatch(1).await();
    }
}
