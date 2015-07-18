package com.codestory2259.yoda.web;

import org.junit.Test;

import static com.codestory2259.yoda.web.RepositoryController.Response;
import static com.codestory2259.yoda.web.utils.RestAssertions.assertThatController;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public class RepositoryControllerTest {

    private final RepositoryController controller = new RepositoryController();

    @Test
    public void mapping() throws Exception {
        assertThatController(controller)
                .onMethod("build")
                .intercept(POST, "/build");

        assertThatController(controller)
                .onMethod("repository")
                .intercept(GET, "/repository/{name}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void errorOnUnknownRepository() throws Exception {
        // when / then
        controller.repository("unknown-repo");
    }

    @Test
    public void getInfoOnRepository() throws Exception {
        // given
        controller.build("dagobah");

        // when
        Response response = controller.repository("dagobah");

        // then
        assertThat(response.name).isEqualTo("dagobah");
        assertThat(response.status).isEqualTo("SUCCESS");
    }

    @Test
    public void getInfoOnAnotherRepository() throws Exception {
        // given
        controller.build("tatooine");

        // when
        Response response = controller.repository("tatooine");

        // then
        assertThat(response.name).isEqualTo("tatooine");
        assertThat(response.status).isEqualTo("SUCCESS");
    }
}
