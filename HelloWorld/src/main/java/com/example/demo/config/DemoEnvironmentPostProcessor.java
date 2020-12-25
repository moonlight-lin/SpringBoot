package com.example.demo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

public class DemoEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        Map<String, Object> defaultMap = new HashMap<>();

        defaultMap.put("server.port", 9000);
        defaultMap.put("user.size", 100);
        defaultMap.put("user.name-length", 20);

        PropertySource<?> propertySource = new MapPropertySource("defaultProp", defaultMap);
        environment.getPropertySources().addLast(propertySource);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
