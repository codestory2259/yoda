package com.codestory2259.yoda.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@EnableAutoConfiguration
public class Rest {

    public static final String JSON = "{}";

    public static void main(String... args) {
        SpringApplication.run(Rest.class);
    }

    public String status() {
        return JSON;
    }
}
