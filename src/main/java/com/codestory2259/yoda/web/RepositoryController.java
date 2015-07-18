package com.codestory2259.yoda.web;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RepositoryController {

    private boolean hasBeenCalled;

    @RequestMapping(method = POST, value = "/build", produces = "application/json")
    public void build(@RequestBody Build build) {
        this.hasBeenCalled = true;
    }

    @RequestMapping(method = GET, value = "/repository/{name}", produces = "application/json")
    public Response repository(@PathVariable String name) {
        if (!hasBeenCalled)
            throw new IllegalArgumentException(format("Unknown repository name `%s`", name));

        return new Response(name, "SUCCESS");
    }

    public static class Build {
        public InnerBuild build = new InnerBuild();

        public static class InnerBuild {
            public String phase;
            public String status;
            public SCM scm = new SCM();

            public static class SCM {
                public String url;
            }
        }
    }

    public static class Response {
        public String name;
        public String status;

        public Response(String name, String status) {
            this.name = name;
            this.status = status;
        }

    }
}
