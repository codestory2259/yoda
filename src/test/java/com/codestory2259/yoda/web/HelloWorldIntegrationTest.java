package com.codestory2259.yoda.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HelloWorld.class)
public class HelloWorldIntegrationTest {

    @Autowired
    private HelloWorld helloWorld;

    @Test
    public void testName() throws Exception {
        assertThat(helloWorld).isNotNull();
        assertThat(helloWorld.sayHello()).isEqualTo("Hey Guys!");
    }
}