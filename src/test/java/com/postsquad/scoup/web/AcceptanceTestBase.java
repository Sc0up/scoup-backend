package com.postsquad.scoup.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.internal.mapping.Jackson2Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTestBase {

    protected static final String BASE_URL = "http://localhost";

    @LocalServerPort
    protected int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void cleanUpDatabase() {
        databaseCleanup.execute();
    }

    @Autowired
    protected ObjectMapper objectMapper;

    // temporary token with sub(userId) = 1, exp = 2023-01-01T00:00:00.000(Korean Standard Time)
    protected static final String TEST_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcyNDk4ODAwfQ.DXojMeUGIq77XWvQK0luZtZhsi-c6s9qjiiu9vHhkbg";

    protected User testUser = User.builder()
                                .nickname("nickname")
                                .email("email@email.com")
                                .password("password")
                                .avatarUrl("url")
                                .username("username")
                                .oAuthUsers(List.of(OAuthUser.of(OAuthType.NONE, "")))
                                .build();

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
