package com.example.demo.controller;

import com.example.demo.annotations.Audit;
import com.example.demo.entity.User;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @Audit("get_all_user_id")
    @GetMapping("/users-id")
    public List<String> getId() {
        return demoService.getUsersId();
    }

    @Audit("get_users")
    @GetMapping("/users")
    public List<User> getUsers(@RequestParam(value = "gender", required = false) String gender) {
        return demoService.getUsers(gender);
    }

    @Audit("create_user")
    @PostMapping("/users/{id}")
    public void createUser(@PathVariable("id") String id,
                           @RequestBody User user) {
        demoService.createUser(id, user);
    }

    @Audit("get_user")
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") String id) {
        return demoService.getUser(id);
    }

    @Audit("update_user")
    @PostMapping("/user/{id}")
    public void updateUser(@PathVariable("id") String id,
                           @RequestBody User user) {
        demoService.updateUser(id, user);
    }
}
