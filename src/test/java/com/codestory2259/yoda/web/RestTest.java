package com.codestory2259.yoda.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class RestTest {

    private final Rest rest = new Rest();

    @Test
    public void statusIsJSON() throws Exception {
        // when
        String status = rest.status();

        // then
        verifyJsonFormat(status);
    }

    @Test
    public void statusSendAlwaysOK() throws Exception {
        // when
        String status = rest.status();

        // then
        assertThat(status).contains("status").contains("OK");
    }

    private void verifyJsonFormat(String status) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(status);
        while (parser.nextToken() != null) ;
    }
}
