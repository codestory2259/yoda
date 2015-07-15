package com.codestory2259.yoda.web;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codestory2259.yoda.web.Rest.Response.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class Rest {

    @RequestMapping(method = GET, value = "/status", produces = "application/json")
    public Response status() {
        return OK;
    }

    public static class Response {
        public static final Response OK = new Response();

        public String status = "OK";
    }
}
