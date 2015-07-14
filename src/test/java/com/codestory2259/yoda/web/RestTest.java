package com.codestory2259.yoda.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static com.codestory2259.yoda.web.utils.RestAssertions.assertThatController;
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
        while (parser.nextToken() != null) doNothing();
    }

    private void doNothing() {}

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
