package com.codestory2259.yoda.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static java.lang.String.format;

@Controller
@EnableAutoConfiguration
public class HelloWorld {

    @Value("${helloworld.name}")
    private String name;

    @RequestMapping("/")
    @ResponseBody
    String sayHello() {
        return format("Hey %s!", name);
    }

    public static void main(String... args) {
        SpringApplication.run(HelloWorld.class, args);
    }

}
