package com.example.demo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @RequestMapping("/hello")
    public String hello() {
        return "consul-service-1 instance-1";
    }
    
    @GetMapping("/product/{id}")
    public String product(@PathVariable("id") String id) {
        return "price of product " + id + " is 100";
    }
}