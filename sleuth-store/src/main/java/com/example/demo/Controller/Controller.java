package com.example.demo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {
	private static Logger log = LoggerFactory.getLogger(Controller.class);
	
    @GetMapping("/store/price/{id}")
    public String price(@PathVariable("id") String id) {
    	log.info("Handling price");
        return "100";
    }
}
