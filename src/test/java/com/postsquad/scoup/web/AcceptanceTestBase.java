package com.postsquad.scoup.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.internal.mapping.Jackson2Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTestBase {

    protected static final String BASE_URL = "http://localhost";

    @LocalServerPort
    protected int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private TestDataSetup testDataSetup;

    @BeforeEach
    void cleanUpDatabase() {
        databaseCleanup.execute();
        testDataSetup.execute();
    }

    @Autowired
    protected ObjectMapper objectMapper;

    protected static final String TEST_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcyNDk4ODAwfQ.DXojMeUGIq77XWvQK0luZtZhsi-c6s9qjiiu9vHhkbg";

    protected String[] ignoringFieldsForResponseWithId = new String[]{"createdDateTime", "modifiedDateTime", "id"};

    protected String[] ignoringFieldsForResponse = new String[]{"createdDateTime", "modifiedDateTime"};

    protected String[] ignoringFieldsForErrorResponse = new String[]{"timestamp"};

    {
        setUpRestAssured();
    }

    private void setUpRestAssured() {
        Jackson2Mapper jackson2Mapper = new Jackson2Mapper((type, charset) -> objectMapper);
        ObjectMapperConfig jackson2ObjectMapperConfig = new ObjectMapperConfig(jackson2Mapper);

        RestAssured.config = RestAssuredConfig.config()
                                              .objectMapperConfig(jackson2ObjectMapperConfig);
    }
}
