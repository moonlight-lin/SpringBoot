package com.example.demo.Entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "user")
public class User {
    private String name;
    private String sex;
    private int age;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name= name;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex= sex;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age= age;
    }
}

