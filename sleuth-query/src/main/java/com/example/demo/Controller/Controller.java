package com.example.demo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class Controller {
    private static Logger log = LoggerFactory.getLogger(Controller.class);
 
    @Autowired
	private RestTemplate restTemplate;
    
    @RequestMapping("/query")
    public String query(@RequestParam(value = "id", required = true) String id){
    	log.info("Handling query"); 
        String forObject = restTemplate.getForObject("http://sleuth-product-service/product/" + id, String.class);
        return forObject;
    }
}
