package com.codestory2259.yoda.web;


import lombok.Data;
import lombok.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RepositoryController {

    private List<Notification> notifications = new ArrayList<>();

    @RequestMapping(method = POST, value = "/notification", produces = "application/json")
    public void notification(@RequestBody Notification notification) {
        if (!"FINALIZED".equals(notification.build.phase)) {
            throw new IllegalArgumentException("Build phase must be `FINALIZED`");
        }

        Optional<Notification> existingNotification = getAnyBranchOnRepo(notification);

        if (existingNotification.isPresent()) {
            existingNotification.get().build.status = notification.build.status;
        } else {
            notifications.add(notification);
        }

    }

    private Optional<Notification> getAnyBranchOnRepo(Notification notification) {
        return createStreamOfNotifications(notification.build.scm.url)
                .filter(n -> n.build.scm.branch.contains(notification.build.scm.branch)).findAny();
    }

    @RequestMapping(method = GET, value = "/repository/{name}", produces = "application/json")
    public Response repository(@PathVariable String name) {
        if (!isRepositoryNameExisting(name))
            throw new IllegalArgumentException(format("Unknown repository name `%s`", name));

        Response response = new Response(name);
        response.status = findLastStatus(name);
        response.branches = createResponseBranches(name);

        return response;
    }

    private boolean isRepositoryNameExisting(String name) {
        return createStreamOfNotifications(name)
                .findAny()
                .isPresent();
    }

    private String findLastStatus(String name) {
        return createStreamOfNotifications(name)
                .reduce((a, b) -> b)
                .get().build.status;
    }

    private List<Response.Branch> createResponseBranches(String name) {
        return createStreamOfNotifications(name)
                .map(build -> new Response.Branch(build.build.scm.branch, build.build.status))
                .collect(Collectors.toList());
    }


    private Stream<Notification> createStreamOfNotifications(String name) {
        return notifications.stream()
                .filter(notification -> notification.build.scm.url.contains(name));
    }

    @Data
    static class Notification {
        Build build = new Build();

        @Data
        class Build {
            String phase;
            String status;
            SCM scm = new SCM();

            @Data
            class SCM {
                String url;
                String branch;
            }
        }
    }

    @Data
    static class Response {
        @NonNull
        String name;
        String status;
        List<Branch> branches = new ArrayList<>();

        @Data
        static class Branch {
            @NonNull
            String name;
            @NonNull
            String status;
        }
    }
}
