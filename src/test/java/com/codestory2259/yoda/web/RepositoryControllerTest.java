package com.codestory2259.yoda.web;

import org.junit.Test;

import static com.codestory2259.yoda.web.RepositoryController.Notification;
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
                .onMethod("notification")
                .intercept(POST, "/notification");

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
        controller.notification(create("dagobah", "SUCCESS", "origin/master"));

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
        controller.notification(create("tatooine", "SUCCESS", "origin/master"));

        // when
        Response response = controller.repository("tatooine");

        // then
        assertThat(response.name).isEqualTo("tatooine");
        assertThat(response.status).isEqualTo("SUCCESS");
    }

    @Test
    public void getInfoWithFailedBuild() throws Exception {
        // given
        controller.notification(create("coruscant", "FAILED", "origin/master"));

        // when
        Response response = controller.repository("coruscant");

        // then
        assertThat(response.name).isEqualTo("coruscant");
        assertThat(response.status).isEqualTo("FAILED");
        assertThat(response.branches).hasSize(1);
        assertThat(response.branches).extracting("name").containsOnly("origin/master");
        assertThat(response.branches).extracting("status").containsOnly("FAILED");
    }

    @Test
    public void severalBranches() throws Exception {
        // given
        controller.notification(create("alderaan", "SUCCESS", "origin/master"));
        controller.notification(create("alderaan", "SUCCESS", "origin/develop"));

        // when
        Response response = controller.repository("alderaan");

        // then
        assertThat(response.branches).hasSize(2);
        assertThat(response.branches).extracting("name").contains("origin/master", "origin/develop");
    }


    @Test
    public void repositoryStatusIsLastBuildStatus() throws Exception {
        // given
        controller.notification(create("hoth", "SUCCESS", "origin/rebels"));
        controller.notification(create("hoth", "FAILED", "origin/empire"));

        // when
        Response response = controller.repository("hoth");

        // then
        assertThat(response.status).isEqualTo("FAILED");
    }

    @Test(expected = IllegalArgumentException.class)
    public void detectUnknownRepositoryFromName() throws Exception {
        //Given
        controller.notification(create("existing", "SUCCESS", "origin/any"));

        //When / then
        controller.repository("unknownRepository");
    }

    @Test
    public void statusDependsOnRepository() throws Exception {
        // Given
        controller.notification(create("repository", "SUCCESS", "origin/branch"));
        controller.notification(create("another", "FAILED", "origin/branch"));

        // When
        Response response = controller.repository("repository");

        // Then
        assertThat(response.status).isEqualTo("SUCCESS");
    }

    @Test
    public void branchesDependOnRepository() throws Exception {
        // Given
        controller.notification(create("repository", "SUCCESS", "origin/branch"));
        controller.notification(create("another", "FAILED", "origin/another-branch"));

        // When
        Response response = controller.repository("repository");

        // Then
        assertThat(response.branches).hasSize(1);
        assertThat(response.branches).extracting("name").contains("origin/branch");
        assertThat(response.branches).extracting("status").contains("SUCCESS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void errorWhenBuildIsNotFinalized() throws Exception {
        // when / then
        controller.notification(createNotFinalized("tatooine"));
    }

    private Notification createNotFinalized(String repository) {
        Notification notification = create(repository, "ANY-STATUS", "origin/master");
        notification.build.phase = "ANOTHER-STATUS";
        return notification;
    }

    private Notification create(String repository, String status, String branch) {
        Notification notification = new Notification();
        notification.build.phase = "FINALIZED";
        notification.build.status = status;
        notification.build.scm.url = repository;
        notification.build.scm.branch = branch;
        return notification;
    }
}
