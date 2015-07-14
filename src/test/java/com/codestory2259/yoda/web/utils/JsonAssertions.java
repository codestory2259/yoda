package com.codestory2259.yoda.web.utils;

import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.AbstractObjectAssert;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonAssertions {

    public static AbstractObjectAssert<?, Object> assertThatJson(String json, String path) {
        String description = format("JSON = %s", json);
        return assertThat(parse(json, path)).as(description);
    }

    private static Object parse(String json, String path) {
        try {
            return JsonPath.read(json, path);
        } catch (Exception cause) {
            String message = format("Unexpected JSON format for `%s`", json);
            throw new IllegalStateException(message, cause);
        }
    }

}
