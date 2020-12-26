package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.User;


@RefreshScope     // 用于自动刷新
@RestController
@RequestMapping("/api/v1")
public class Controller {
    @Value("${database-host}")
    private String dbHost;

    @Autowired
    private User user;

    @GetMapping(value = "db")
    public String getDB(){
        return dbHost;
    }

    @GetMapping(value = "user")
    public Object getUser(){
        return user;
    }
}
