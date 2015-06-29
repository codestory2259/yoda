package com.codestory2259.yoda.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HelloWorld.class)
@WebIntegrationTest("server.port=9000")
public class HelloWorldIntegrationTest {

    @Autowired
    private HelloWorld helloWorld;

    private RestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void useLoadedObject() throws Exception {
        assertThat(helloWorld).isNotNull();
        assertThat(helloWorld.sayHello()).isEqualTo("Hey Guys!");
    }

    @Test
    public void useRestToCheck() throws Exception {
        String message = restTemplate.getForObject("http://localhost:9000", String.class);
        assertThat(message).isEqualTo("Hey Guys!");
    }
}