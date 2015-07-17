package com.codestory2259.yoda.web;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.codestory2259.yoda.web.Rest.Response.OK;
import static java.lang.String.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class Rest {

    private boolean hasBeenCalled;

    @RequestMapping(method = GET, value = "/status", produces = "application/json")
    public Response status() {
        return OK;
    }

    @RequestMapping(method = POST, value = "/build", produces = "application/json")
    public void build(@RequestBody String body) {
        this.hasBeenCalled = true;
    }

    @RequestMapping(method = GET, value = "/repository", produces = "application/json")
    public RepositoryResponse repository(@RequestParam(value = "name")  String name) {
        if (!hasBeenCalled)
           throw new IllegalArgumentException(format("Unknown repository name `%s`", name));

        RepositoryResponse response = new RepositoryResponse();
        response.name = name;
        response.status = "SUCCESS";

        return response;

    }

    public static class Response {
        public static final Response OK = new Response();

        public String status = "OK";
    }

    public class RepositoryResponse {
        public String name;
        public String status;
    }
}
