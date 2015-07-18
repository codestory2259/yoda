package com.codestory2259.yoda.itest;

import com.codestory2259.yoda.MainClass;
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
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest("server.port=9000")
@SpringApplicationConfiguration(classes = MainClass.class)
public class RestIntegrationTest {

    private static final String EMPTY = "";

    private final RestTemplate restTemplate = new RestTemplate();

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
        send(POST, "/build", "{\"build\":{\"phase\": \"COMPLETED\",\"status\":\"SUCCESS\",\"scm\":{\"url\":\"http://server/my-awesome-project.git\"}}}");

        // then
        String response = send(GET, "/repository/my-awesome-project");
        assertThatJson(response, "$.name").isEqualTo("my-awesome-project");
        assertThatJson(response, "$.status").isEqualTo("SUCCESS");
    }

    @Test(expected = HttpServerErrorException.class)
    public void ignoreNotFinalizedBuild() throws Exception {
        // when
        send(POST, "/build", "{\"build\":{\"phase\": \"STARTED\",\"status\":\"ANY-STATUS\",\"scm\":{\"url\":\"http://server/ignored-project.git\"}}}");
    }

    private String send(HttpMethod httpMethod, String url) throws URISyntaxException {
        return send(httpMethod, url, EMPTY);
    }

    private String send(HttpMethod httpMethod, String url, String body) throws URISyntaxException {
        URI uri = new URI("http://localhost:9000" + url);
        RequestEntity entity = RequestEntity.method(httpMethod, uri).contentType(APPLICATION_JSON).body(body);
        ResponseEntity<String> response = restTemplate.exchange(entity, String.class);
        return response.getBody();
    }
}
