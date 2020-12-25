package com.example.demo.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String gender;
    private int age;
    private float salary;

    // 这个空的构造函数是必须的，不然 Controller 无法将 request 的 body 取出
    public User() {
    }

    public User(String name, String gender, int age, float salary) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.salary = salary;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public int getAge() {
        return this.age;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }
    public float getSalary() {
        return this.salary;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getGender() {
        return this.gender;
    }
}
