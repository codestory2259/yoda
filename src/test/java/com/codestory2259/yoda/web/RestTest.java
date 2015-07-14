package com.codestory2259.yoda.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class RestTest {

    private final Rest rest = new Rest();

    @Test
    public void statusIsJSON() throws Exception {
        // when
        String status = rest.status();

        // then
        verifyJsonFormat(status);
    }

    private void verifyJsonFormat(String status) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(status);
        while (parser.nextToken() != null) ;
    }

    @Test
    public void statusSendAlwaysOK() throws Exception {
        // when
        String status = rest.status();

        // then
        assertThat(status).contains("status").contains("OK");
    }

    @Test
    public void restMapping() throws Exception {

        Class<? extends Rest> type = rest.getClass();
        String expectedName = "status";




        RestController annotation = type.getAnnotation(RestController.class);
        assertThat(annotation).as("The class must be annotated @RestController").isNotNull();

        Method method = Arrays.stream(type.getMethods())
                .filter(m -> m.getName().equals(expectedName))
                .findFirst()
                .get();

        assertThat(method).as("Method must exist").isNotNull();

        RequestMapping annotation2 = method.getAnnotation(RequestMapping.class);
        assertThat(annotation2).as("The method must be annotated @RequestMapping").isNotNull();
        assertThat(annotation2.produces()).contains("application/json");
    }
}
