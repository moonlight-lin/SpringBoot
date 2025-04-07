package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@EnableScheduling
@Slf4j
public class SchedulerConfig {

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public TaskScheduler taskScheduler() {
        // scheduler 默认只有一个 thread，如果要使用线程池，就需要自定义一个
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadNamePrefix("My-Scheduler-");
        taskScheduler.initialize();
        return taskScheduler;
    }

    // 不能放到 ConfigAutoRefresh 类里，不然定时不准，可能 10 分钟都不会触发，怀疑是受 @RefreshScope 干扰
    @Scheduled(initialDelay = 10000, fixedDelay = 60000)
    public void triggerConfigRefresh() {
        log.info("triggerConfigRefresh");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(null, headers);

        // 给本机发送 refresh 请求，触发应用从 Spring Cloud Config Server 重新获取配置数据
        restTemplate.exchange("http://localhost:9999/refresh", HttpMethod.POST, entity, Void.class);

        log.info("Config refreshed");
    }
}
