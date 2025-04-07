package com.example.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RefreshScope   // 可以被 refresh 接口触发自动刷新
@Data
public class ConfigAutoRefresh {
    @Value("${feature.test.value:default-value}")
    private String featureValue;

    // 能获取 userList: user-a,user-b
    // 不能获取 userList:
    //          - user-a
    //          - user-b
    @Value("${feature.test.userList:}")
    private List<String> userList;
}
