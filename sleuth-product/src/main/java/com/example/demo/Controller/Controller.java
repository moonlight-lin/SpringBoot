package com.example.demo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class Controller {
	private static Logger log = LoggerFactory.getLogger(Controller.class);
	
    @Autowired
    private RestTemplate restTemplate;
	
    @GetMapping("/product/{id}")
    public String product(@PathVariable("id") String id) {
    	log.info("Handling product");
    	String forObject = restTemplate.getForObject("http://sleuth-store-service/store/price/" + id, String.class);
        return "price of product " + id + " is " + forObject;
    }
}
