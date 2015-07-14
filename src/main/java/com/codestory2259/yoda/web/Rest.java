package com.codestory2259.yoda.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Rest {

    public static final String STATUS_OK = "{\"status\":\"OK\"}";

    public static void main(String... args) {
        SpringApplication.run(Rest.class);
    }

    @RequestMapping(value = "/status", produces = "application/json")
    public String status() {
        return STATUS_OK;
    }
}
