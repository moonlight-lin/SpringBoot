package com.example.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class ConfigNoRefresh {    // 没有 @RefreshScope 不会被 refresh 接口触发自动刷新
    @Value("${feature.test.value:default-value}")
    private String featureValue;

    // 能获取 userList: user-a,user-b
    // 不能获取 userList:
    //          - user-a
    //          - user-b
    @Value("${feature.test.userList:}")
    private List<String> userList;
}
