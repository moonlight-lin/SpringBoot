package com.example.demo.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;


@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced  // 实现 Ribbon 负载均衡的 RestTemplate
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public IRule loadbalanceRule(){
        return new RandomRule();   // 随机策略
    }
}
