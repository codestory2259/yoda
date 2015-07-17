package com.codestory2259.yoda.web;

import org.junit.Test;

import static com.codestory2259.yoda.web.utils.RestAssertions.assertThatController;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

        assertThatController(rest)
                .onMethod("build")
                .intercept(POST, "/build");

        assertThatController(rest)
                .onMethod("repository")
                .intercept(GET, "/repository/{name}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void errorOnUnknownRepository() throws Exception {
        // when / then
        rest.repository("unknown-repo");
    }

    @Test
    public void getInfoOnRepository() throws Exception {
        // given
        rest.build("dagobah");

        // when
        Rest.RepositoryResponse response = rest.repository("dagobah");

        // then
        assertThat(response.name).isEqualTo("dagobah");
        assertThat(response.status).isEqualTo("SUCCESS");
    }

    @Test
    public void getInfoOnAnotherRepository() throws Exception {
        // given
        rest.build("tatooine");

        // when
        Rest.RepositoryResponse response = rest.repository("tatooine");

        // then
        assertThat(response.name).isEqualTo("tatooine");
        assertThat(response.status).isEqualTo("SUCCESS");
    }
}
