package com.example.demo.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced  // 实现 Ribbon 负载均衡的 RestTemplate
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
