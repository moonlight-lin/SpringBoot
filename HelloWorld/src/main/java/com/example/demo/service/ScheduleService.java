package com.example.demo.service;

import com.example.demo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.List;

@EnableScheduling
@Component
public class ScheduleService {
    @Autowired
    private DemoService demoService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //@Scheduled(cron = "0 0/2 * * * ?")
    @Scheduled(initialDelayString = "5000", fixedDelayString = "100000")
    public void saveUserToDB() {
        List<User> user = demoService.getUsers(null);
        // TODO: save user to database
        logger.info("schedule : save user list to database");
    }

    // 配置线程池
    // 不知道写在这里有没有用，可能写到一个专门初始化配置的类比较好
    @Bean
    public TaskScheduler configTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        return scheduler;
    }
}
