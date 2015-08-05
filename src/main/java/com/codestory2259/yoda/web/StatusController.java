package com.codestory2259.yoda.web;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @Data
    public static class Response {
        String response = "Through the Force, things you will see. Other places. The future... the past. Old friends long gone.";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/status", produces = "application/json")
    public Response status() {
        return new Response();
    }
}
