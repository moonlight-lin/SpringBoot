package com.example.dubbo.consumer;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import com.example.dubbo.interfaces.GreetingService;

public class Consumer {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "localhost");

    public static void main(String[] args) {
        ReferenceConfig<GreetingService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        reference.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        reference.setInterface(GreetingService.class);
        GreetingService service = reference.get();
        String message = service.sayHi("dubbo");
        System.out.println(message);
    }
}
