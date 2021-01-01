package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.feign.DemoFeignClient;


@RestController
public class Controller {
    @Autowired
    private DemoFeignClient demoFeignClient;
	
    @RequestMapping("/query/{id}")
    public String qurey(@PathVariable Long id) {
        // 调用 Feign Client，本质上还是调用 RestTemplate 的方式，Feign 会进行转化
        return demoFeignClient.findProductById(id);
    }
}

