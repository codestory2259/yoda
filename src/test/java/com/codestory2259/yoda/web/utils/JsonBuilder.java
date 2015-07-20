package com.codestory2259.yoda.web.utils;

import static java.lang.String.format;

public class JsonBuilder {
    private String phase = "COMPLETED";
    private String status = "SUCCESS";
    private String repository = "http://any.server/repository.git";

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

    public JsonBuilder repository(String repository) {
        this.repository = repository;
        return this;
    }

    public String get() {
        String scm = format("{\"url\":\"%s\"}", repository);
        String innerBuild = format("{\"phase\":\"%s\", \"status\":\"%s\", \"scm\":%s}", phase, status, scm);
        return format("{\"name\":\"jenkins\", \"build\":%s}", innerBuild);
    }
}
