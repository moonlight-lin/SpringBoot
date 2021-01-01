package com.example.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="consul-service-1")
public interface DemoFeignClient {
    // 这个函数会自动从 Consul 获取 producer consul-service-1 的信息，然后发送请求到相应的 URL
    @RequestMapping(value="/product/{id}", method = RequestMethod.GET)
    public String findProductById(@PathVariable("id") Long id);
}
