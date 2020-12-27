package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {
    @Autowired
    private LoadBalancerClient loadBalancer;
    
    @Autowired
    private DiscoveryClient discoveryClient;
 
    @Autowired
    private RestTemplate restTemplate;  // 具有负载均衡的restTemplate
    
    @RequestMapping("/services")
    public Object services() {
        // 获取 consul-service-1 服务的信息
        // 这里会返回两个，因为有两个服务注册了这个名字
        return discoveryClient.getInstances("consul-service-1");
    }
 
    @RequestMapping("/discover")
    public Object discover() {
        // 通过 LoadBalancerClient 获取 consul-service-1 服务的其中一个 host
        // 可以看到有时返回 8501 端口，有时返回 8502 端口，这样就实现了负载均衡
        return loadBalancer.choose("consul-service-1").getUri().toString();
    }
    
    @RequestMapping("/rest")
    public String rest(){
        // 这里直接使用注册在 Consul 的 service id 也就是 consul-service-1
        // 这样 RestTemplate 会自动实现负载均衡
        String forObject = restTemplate.getForObject("http://consul-service-1/hello", String.class);
        return forObject;
    }
}