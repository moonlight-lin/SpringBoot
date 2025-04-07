package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "feature.test")
@Data
public class ConfigProperty {   // 没有 @RefreshScope 也会被 refresh 接口触发自动刷新，和 @Value 不一样
    private String value;
    private List<String> featureList;

    // 两种格式都能获取
    // userList: user-a,user-b
    // userList:
    //   - user-a
    //   - user-b
    private List<String> userList;
}
