package com.codestory2259.yoda.web.utils;

import static java.lang.String.format;

public class JsonBuilder {
    private String phase = "FINALIZED";
    private String status = "SUCCESS";
    private String repository = "http://any.server/repository.git";
    private String branch = "origin/any-branch";

    public static JsonBuilder createBuild() {
        return new JsonBuilder();
    }

    public JsonBuilder phase(String phase) {
        this.phase = phase;
        return this;
    }

    public JsonBuilder status(String status) {
        this.status = status;
        return this;
    }

    public JsonBuilder repository(String name) {
        this.repository = format("http://server/%s.git", name);
        return this;
    }

    public JsonBuilder branch(String branch) {
        this.branch = branch;
        return this;
    }

    public String get() {
        String scm = format("{\"url\":\"%s\", \"branch\":\"%s\"}", repository, branch);
        String innerBuild = format("{\"phase\":\"%s\", \"status\":\"%s\", \"scm\":%s}", phase, status, scm);
        return format("{\"name\":\"jenkins\", \"build\":%s}", innerBuild);
    }
}
