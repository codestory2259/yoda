package com.codestory2259.yoda.web.utils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Optional;

import static java.util.Arrays.stream;
import static org.assertj.core.api.StrictAssertions.assertThat;

public class RestAssertions {

    private final Class<?> controllerType;

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

    public void map(String methodName) {
        Method method = findMethod(methodName);

        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        assertThat(annotation).as("The method must be annotated @RequestMapping").isNotNull();
        assertThat(annotation.produces()).contains("application/json");
    }

    private Method findMethod(String methodName) {
        Optional<Method> method = stream(controllerType.getMethods())
                .filter(m -> m.getName().equals(methodName))
                .findFirst();

        assertThat(method.isPresent()).as("Method must exist").isTrue();
        return method.get();
    }
}
