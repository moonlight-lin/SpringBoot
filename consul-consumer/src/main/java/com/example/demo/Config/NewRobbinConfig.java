package com.example.demo.Config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClient(name="consul-service-1",configuration=RestTemplateConfig.class)
public class NewRobbinConfig {

}