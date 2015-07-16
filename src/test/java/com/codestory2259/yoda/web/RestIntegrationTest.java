package com.codestory2259.yoda.web;

import com.codestory2259.yoda.MainClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static com.codestory2259.yoda.web.utils.JsonAssertions.assertThatJson;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest("server.port=9000")
@SpringApplicationConfiguration(classes = MainClass.class)
public class RestIntegrationTest {

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void statusResponse() throws Exception {
        // when
        String response = restTemplate.getForObject("http://localhost:9000/status", String.class);

        // then
        assertThatJson(response, "$.status").isEqualTo("OK");
    }

    @Test
    public void retrieveBuildFromJenkins() throws Exception {
        // when
        restTemplate.postForObject("http://localhost:9000/build", "{\"name\":\"jenkins\",\"build\":{\"status\":\"SUCCESS\",\"scm\":{\"url\":\"http://monrepo.git\"}}}", Void.class);

        // then
        String response = restTemplate.getForObject("http://localhost:9000/repository/monrepo", String.class);
        assertThatJson(response, "$.name").isEqualTo("monrepo");
        assertThatJson(response, "$.status").isEqualTo("SUCCESS");
    }
}
