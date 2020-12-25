package com.example.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "user")
@Validated
public class UserProperties {
    @NotNull
    private int size;

    @Min(5)
    @Max(20)
    private int nameLength = 10;

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public int getNameLength() {
        return nameLength;
    }
    public void setNameLength(int nameLength) {
        this.nameLength = nameLength;
    }
}
