package com.codestory2259.yoda.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

public class RestTest {

    @Test
    public void statusIsJSON() throws Exception {
        // given
        Rest rest = new Rest();

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
}
