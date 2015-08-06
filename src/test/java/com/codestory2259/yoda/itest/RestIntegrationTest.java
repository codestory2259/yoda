package com.codestory2259.yoda.itest;

import com.codestory2259.yoda.MainClass;
import com.codestory2259.yoda.web.utils.JsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static com.codestory2259.yoda.web.utils.JsonAssertions.assertThatJson;
import static com.codestory2259.yoda.web.utils.JsonBuilder.createBuild;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest("server.port=9000")
@SpringApplicationConfiguration(classes = MainClass.class)
public class RestIntegrationTest {

    private static final JsonBuilder EMPTY = null;

    private final RestTemplate restTemplate = new RestTemplate();

    private String response;

    @Test
    public void statusResponse() throws Exception {
        // when
        String response = send(GET, "/status");

        // then
        assertThatJson(response, "$.response").isNotEmpty();
    }

    @Test
    public void retrieveFirstBuildFromJenkins() throws Exception {
        // when
        send(POST, "/notification", createBuild().status("SUCCESS").repository("my-awesome-project"));

        // then
        response = send(GET, "/repository/my-awesome-project");
        assertThatJson(response, "$.name").isEqualTo("my-awesome-project");
        assertThatJson(response, "$.status").isEqualTo("SUCCESS");
    }

    @Test
    public void statusForSpecificBranches() throws Exception {
        // when
        send(POST, "/notification", createBuild().repository("success-and-failed").branch("origin/master").status("SUCCESS"));
        send(POST, "/notification", createBuild().repository("success-and-failed").branch("origin/ugly-fix").status("FAILED"));

        // then
        response = send(GET, "/repository/success-and-failed");
        assertThatJson(response, "$.name").isEqualTo("success-and-failed");
        assertThatJson(response, "$.status").isEqualTo("FAILED");

        assertThatJson(response, "$.branches[0].name").isEqualTo("origin/master");
        assertThatJson(response, "$.branches[0].status").isEqualTo("SUCCESS");

        assertThatJson(response, "$.branches[1].name").isEqualTo("origin/ugly-fix");
        assertThatJson(response, "$.branches[1].status").isEqualTo("FAILED");
    }

    @Test
    public void statusForTwoBranches() throws Exception {
        // given
        send(POST, "/notification", createBuild().repository("first").branch("origin/my-branch").status("SUCCESS"));
        send(POST, "/notification", createBuild().repository("second").branch("origin/another-branch").status("FAILED"));

        // when / then (first repository)
        response = send(GET, "/repository/first");
        assertThatJson(response, "$.name").isEqualTo("first");
        assertThatJson(response, "$.status").isEqualTo("SUCCESS");
        assertThatJson(response, "$.branches[0].name").isEqualTo("origin/my-branch");
        assertThatJson(response, "$.branches[0].status").isEqualTo("SUCCESS");

        // when / then (second repository)
        response = send(GET, "/repository/second");
        assertThatJson(response, "$.name").isEqualTo("second");
        assertThatJson(response, "$.status").isEqualTo("FAILED");
        assertThatJson(response, "$.branches[0].name").isEqualTo("origin/another-branch");
        assertThatJson(response, "$.branches[0].status").isEqualTo("FAILED");
    }

    @Test(expected = HttpServerErrorException.class)
    public void ignoreNotFinalizedBuild() throws Exception {
        // when
        send(POST, "/notification", createBuild().phase("STARTED"));
    }

    @Test
    public void statusForSameBranches() throws Exception {
        // when
        send(POST, "/notification", createBuild().repository("same-branches").branch("origin/master").status("SUCCESS"));
        send(POST, "/notification", createBuild().repository("same-branches").branch("origin/master").status("FAILED"));
        send(POST, "/notification", createBuild().repository("same-branches").branch("origin/develop").status("SUCCESS"));
        send(POST, "/notification", createBuild().repository("same-branches").branch("origin/develop").status("FAILED"));

        // then
        response = send(GET, "/repository/same-branches");
        assertThatJson(response, "$.name").isEqualTo("same-branches");
        assertThatJson(response, "$.status").isEqualTo("FAILED");

        assertThatJson(response, "$.branches[0].name").isEqualTo("origin/master");
        assertThatJson(response, "$.branches[0].status").isEqualTo("FAILED");

        assertThatJson(response, "$.branches[1].name").isEqualTo("origin/develop");
        assertThatJson(response, "$.branches[1].status").isEqualTo("FAILED");

    }

    private String send(HttpMethod httpMethod, String url) throws URISyntaxException {
        return send(httpMethod, url, EMPTY);
    }

    private String send(HttpMethod httpMethod, String url, JsonBuilder body) throws URISyntaxException {
        String json = body != null ? body.get() : "";

        URI uri = new URI("http://localhost:9000" + url);
        RequestEntity entity = RequestEntity.method(httpMethod, uri).contentType(APPLICATION_JSON).body(json);
        ResponseEntity<String> response = restTemplate.exchange(entity, String.class);
        return response.getBody();
    }

}
