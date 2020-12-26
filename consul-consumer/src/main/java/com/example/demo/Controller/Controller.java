package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	@Autowired
    private LoadBalancerClient loadBalancer;
	
    @Autowired
    private DiscoveryClient discoveryClient;
 
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
}