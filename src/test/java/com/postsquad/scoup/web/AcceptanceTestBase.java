package com.postsquad.scoup.web;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTestBase {

    protected static final String BASE_URL = "http://localhost";

    @LocalServerPort
    protected int port;
}
