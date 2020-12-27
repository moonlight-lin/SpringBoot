package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Controller {
    @Autowired
    private LoadBalancerClient loadBalancer;
    
    @Autowired
    private DiscoveryClient discoveryClient;
 
    @Autowired
    private RestTemplate restTemplate;  // 具有负载均衡的 restTemplate
    
    @RequestMapping("/queryService")
    public String query() {
        List<ServiceInstance> instances = discoveryClient.getInstances("service-provider-1");
        StringBuilder urls= new StringBuilder();
        for(ServiceInstance instance : instances){
            urls.append(instance.getHost()+":"+instance.getPort()).append(",");
        }
        return "service name : service-provider-1<br>" + "host : " + urls.toString();
    }
    
    @RequestMapping("/discover")
    public Object discover() {
        // 通过 LoadBalancerClient 获取 service-provider-1 服务的其中一个 host
        // 可以看到有时返回 8762 端口，有时返回 8763 端口，这样就实现了负载均衡
        return loadBalancer.choose("service-provider-1").getUri().toString();
    }
    
    @RequestMapping("/rest")
    public String rest() {
        // 这里直接使用注册在 Eureka 的 service id 也就是 service-provider-1
        // 这样 RestTemplate 会自动实现负载均衡
        String forObject = restTemplate.getForObject("http://service-provider-1/api", String.class);
        return forObject;
    }
}
