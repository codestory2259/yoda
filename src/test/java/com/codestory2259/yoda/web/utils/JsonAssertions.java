package com.codestory2259.yoda.web.utils;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;

import static com.jayway.jsonpath.JsonPath.read;

public class JsonAssertions {

    public static AbstractObjectAssert<?, Object> assertThatJson(String response, String query) {
        Object read = read(response, query);
        return Assertions.assertThat(read);
    }

}
