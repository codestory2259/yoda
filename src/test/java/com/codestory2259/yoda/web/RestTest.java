package com.codestory2259.yoda.web;

import org.junit.Test;

import static com.codestory2259.yoda.web.utils.RestAssertions.assertThatController;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

public class RestTest {

    private final Rest rest = new Rest();

    @Test
    public void statusSendAlwaysOK() throws Exception {
        // when
        Rest.Response response = rest.status();

        // then
        assertThat(response).isNotNull();
        assertThat(response.status).isEqualTo("OK");
    }

    @Test
    public void restMapping() throws Exception {
        assertThatController(rest)
                .onMethod("status")
                .intercept(GET, "/status");
    }

}
