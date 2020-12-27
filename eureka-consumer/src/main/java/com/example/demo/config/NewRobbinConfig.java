package com.example.demo.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClient(name="service-provider-1",configuration=RestTemplateConfig.class)
public class NewRobbinConfig {

}