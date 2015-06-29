package com.codestory2259.yoda.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HelloWorld.class)
public class HelloWorldIntegrationTest {

    @Autowired
    private HelloWorld helloWorld;

    @Test
    public void testName() throws Exception {
        assertThat(helloWorld, is(notNullValue()));
        assertThat(helloWorld.sayHello(), is(equalTo("Hey Guys!")));
    }
}