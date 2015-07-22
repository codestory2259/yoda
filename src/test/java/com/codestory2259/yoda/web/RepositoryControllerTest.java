package com.codestory2259.yoda.web;

import org.junit.Test;

import static com.codestory2259.yoda.web.RepositoryController.Build;
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
        controller.build(createBuild("dagobah", "SUCCESS", "origin/master"));

        // when
        Response response = controller.repository("dagobah");

        // then
        assertThat(response.name).isEqualTo("dagobah");
        assertThat(response.status).isEqualTo("SUCCESS");
        assertThat(response.branches).hasSize(1);
        assertThat(response.branches).extracting("name").containsOnly("origin/master");
        assertThat(response.branches).extracting("status").containsOnly("SUCCESS");
    }

    @Test
    public void getInfoOnAnotherRepository() throws Exception {
        // given
        controller.build(createBuild("tatooine", "SUCCESS", "origin/master"));

        // when
        Response response = controller.repository("tatooine");

        // then
        assertThat(response.name).isEqualTo("tatooine");
        assertThat(response.status).isEqualTo("SUCCESS");
    }

    @Test
    public void getInfoWithFailedBuild() throws Exception {
        // given
        controller.build(createBuild("coruscant", "FAILED", "origin/master"));

        // when
        Response response = controller.repository("coruscant");

        // then
        assertThat(response.name).isEqualTo("coruscant");
        assertThat(response.status).isEqualTo("FAILED");
        assertThat(response.branches).hasSize(1);
        assertThat(response.branches).extracting("name").containsOnly("origin/master");
        assertThat(response.branches).extracting("status").containsOnly("FAILED");
    }

    @Test(expected = IllegalArgumentException.class)
    public void notCompletedBuild() throws Exception {
        // when / then
        controller.build(createUnconmpletedBuild("tatooine"));
    }

    private Build createUnconmpletedBuild(String repository) {
        Build build = createBuild(repository, "ANY-STATUS", "origin/master");
        build.build.phase = "STARTED";
        return build;
    }

    private Build createBuild(String repository, String status, String branch) {
        Build build = new Build();
        build.build.phase = "COMPLETED";
        build.build.status = status;
        build.build.scm.url = repository;
        build.build.scm.branch = branch;
        return build;
    }
}
