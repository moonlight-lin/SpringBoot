package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.DemoException;
import com.example.demo.properties.UserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DemoService {
    @Autowired
    private UserProperties userProperties;

    private Map<String, User> users = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        users.put("1", new User("Lin", "male", 30, 20000));
        users.put("2", new User("Zhao", "female", 25, 10000));
    }

    public User getUser(String id) {
        if (! users.containsKey(id)) {
            throw new DemoException("Demo-40001", "User not exist");
        }
        return users.get(id);
    }

    public List<String> getUsersId() {
        return new ArrayList<>(users.keySet());
    }

    public List<User> getUsers(String gender) {
        if (gender != null) {
            if (!gender.equals("male") && !gender.equals("female") ) {
                throw new DemoException("Demo-40005", "Invalid gender");
            }

            return users.values().stream()
                    .filter(user -> user.getGender().equals(gender))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<> (users.values());
        }
    }

    public void createUser(String id, User user) {
        if (users.containsKey(id)) {
            throw new DemoException("Demo-40002", "User already exist");
        } else if (userProperties.getSize() <= users.size()) {
            throw new DemoException("Demo-40003", "User db is full");
        } else if (userProperties.getNameLength() <= user.getName().length()) {
            throw new DemoException("Demo-40004", "User name must <= " + userProperties.getNameLength());
        }

        users.put(id, user);
    }

    public void updateUser(String id, User user) {
        if (! users.containsKey(id)) {
            throw new DemoException("Demo-40001", "User not exist");
        } else if (userProperties.getNameLength() <= user.getName().length()) {
            throw new DemoException("Demo-40004", "User name must <= " + userProperties.getNameLength());
        }
        users.put(id, user);
    }

}
