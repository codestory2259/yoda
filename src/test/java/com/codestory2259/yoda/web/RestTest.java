package com.codestory2259.yoda.web;

import org.junit.Test;

import static com.codestory2259.yoda.web.utils.RestAssertions.assertThatController;
import static org.assertj.core.api.Assertions.assertThat;

public class RestTest {

    private final Rest rest = new Rest();

    @Test
    public void statusSendAlwaysOK() throws Exception {
        // when
        String status = rest.status();

        // then
        assertThat(status).contains("status").contains("OK");
    }

    @Test
    public void restMapping() throws Exception {
        assertThatController(rest).map("status");
    }

}
