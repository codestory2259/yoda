package com.codestory2259.yoda.web.utils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Optional;

import static java.util.Arrays.stream;
import static org.assertj.core.api.StrictAssertions.assertThat;

public class RestAssertions {

    private final Class<?> controllerType;
    private Method method;

    public RestAssertions(Class<?> controllerType) {
        this.controllerType = controllerType;
    }

    public static RestAssertions assertThatController(Object controller) {
        assertThat(controller).isNotNull();
        return assertThatController(controller.getClass());
    }

    public static RestAssertions assertThatController(Class<?> type) {
        assertThat(type).isNotNull();

        RestController annotation = type.getAnnotation(RestController.class);
        assertThat(annotation).as("The class must be annotated @RestController").isNotNull();

        return new RestAssertions(type);
    }

    public RestAssertions onMethod(String name) {
        method = findMethod(name);

        return this;
    }

    private Method findMethod(String expectedName) {
        Optional<Method> method = stream(controllerType.getMethods())
                .filter(m -> m.getName().equals(expectedName))
                .findFirst();

        assertThat(method.isPresent()).as("Method must exist").isTrue();

        return method.get();
    }

    public void intercept(RequestMethod requestMethod, String url) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        assertThat(annotation).as("The method must be annotated @RequestMapping").isNotNull();
        assertThat(annotation.value()).containsOnly(url);
        assertThat(annotation.method()).contains(requestMethod);
        assertThat(annotation.produces()).contains("application/json");
    }
}
