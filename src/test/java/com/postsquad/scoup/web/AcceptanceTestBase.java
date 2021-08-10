package com.postsquad.scoup.web;

import io.restassured.internal.mapping.Jackson2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTestBase {

    protected static final String BASE_URL = "http://localhost";

    @LocalServerPort
    protected int port;

    @Autowired
    protected com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper;

    protected io.restassured.mapper.ObjectMapper restAssuredObjectMapper = new Jackson2Mapper((type, charset) -> jacksonObjectMapper);
}
