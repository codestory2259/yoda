package com.codestory2259.yoda.web;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class Rest {

    private boolean hasBeenCalled;

    @RequestMapping(method = POST, value = "/build", produces = "application/json")
    public void build(@RequestBody String body) {
        this.hasBeenCalled = true;
    }

    @RequestMapping(method = GET, value = "/repository/{name}", produces = "application/json")
    public RepositoryResponse repository(@PathVariable String name) {
        if (!hasBeenCalled)
           throw new IllegalArgumentException(format("Unknown repository name `%s`", name));

        RepositoryResponse response = new RepositoryResponse();
        response.name = name;
        response.status = "SUCCESS";

        return response;

    }

    public class RepositoryResponse {
        public String name;
        public String status;
    }
}
