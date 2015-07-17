package com.codestory2259.yoda.web;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.codestory2259.yoda.web.utils.RestAssertions.assertThatController;
import static org.assertj.core.api.Assertions.assertThat;

public class StatusControllerTest {

    private final StatusController controller = new StatusController();

    @Test
    public void mapping() throws Exception {
        assertThatController(controller)
                .onMethod("status")
                .intercept(RequestMethod.GET, "/status");
    }

    @Test
    public void response() throws Exception {
        // when
        StatusController.Response response = controller.status();

        // then
        assertThat(response.response).isNotEmpty();
    }
}
