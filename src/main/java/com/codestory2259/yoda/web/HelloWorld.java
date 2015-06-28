package com.codestory2259.yoda.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class HelloWorld {

    @RequestMapping("/")
    @ResponseBody
    String sayHello() {
        return "Hey guys!";
    }

    public static void main(String... args) {
        SpringApplication.run(HelloWorld.class, args);
    }

}