package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Controller {
	@Autowired
    private DiscoveryClient discoveryClient;
 
    @RequestMapping("/queryService")
    public String query() {
        List<ServiceInstance> instances = discoveryClient.getInstances("service-provider-1");
        StringBuilder urls= new StringBuilder();
        for(ServiceInstance instance : instances){
            urls.append(instance.getHost()+":"+instance.getPort()).append(",");
        }
        return "service name : service-provider-1<br>" + "host : " + urls.toString();
    }
}
