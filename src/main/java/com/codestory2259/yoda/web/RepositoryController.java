package com.codestory2259.yoda.web;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RepositoryController {

    private List<Build> builds = new ArrayList<>();

    @RequestMapping(method = POST, value = "/notification", produces = "application/json")
    public void notification(@RequestBody Build build) {
        if (!"COMPLETED".equals(build.build.phase)) {
            throw new IllegalArgumentException("Build phase must be `COMPLETED`");
        }

        builds.add(build);
    }

    @RequestMapping(method = GET, value = "/repository/{name}", produces = "application/json")
    public Response repository(@PathVariable String name) {
        if(!isRepositoryNameExisting(name))
            throw new IllegalArgumentException(format("Unknown repository name `%s`", name));

        Response response = new Response(name);
        response.status = findLastStatus(name);
        response.branches = createResponseBranches(name);

        return response;
    }

    private boolean isRepositoryNameExisting(String name) {
        return createStreamOfBuilds(name)
                .findAny()
                .isPresent();
    }

    private String findLastStatus(String name) {
        return createStreamOfBuilds(name)
                .reduce((a, b) -> b)
                .get().build.status;
    }

    private List<Response.Branch> createResponseBranches(String name) {
        return createStreamOfBuilds(name)
                .map(build -> new Response.Branch(build.build.scm.branch, build.build.status))
                .collect(Collectors.toList());
    }

    private Stream<Build> createStreamOfBuilds(String name) {
        return builds.stream().filter(build -> build.build.scm.url.contains(name));
    }

    public static class Build {
        public InnerBuild build = new InnerBuild();

        public static class InnerBuild {
            public String phase;
            public String status;
            public SCM scm = new SCM();

            public static class SCM {
                public String url;
                public String branch;
            }
        }
    }

    public static class Response {
        public String name;
        public String status;
        public List<Branch> branches = new ArrayList<>();

        public Response(String name) {
            this.name = name;
        }

        private static class Branch {
            public String name;
            public String status;

            public Branch(String name, String status) {
                this.name = name;
                this.status = status;
            }

            public String getName() {
                return name;
            }

            public String getStatus() {
                return status;
            }
        }
    }
}
