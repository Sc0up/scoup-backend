package com.postsquad.scoup.web.schedule;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.TestEntityManager;
import com.postsquad.scoup.web.schedule.controller.request.SchedulePollRequest;
import com.postsquad.scoup.web.schedule.controller.response.SchedulePollResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class SchedulePollAcceptanceTest extends AcceptanceTestBase {

    private static final Snippet SCHEDULE_POLL_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID"),
            parameterWithName("scheduleId")
                    .description("스케줄 ID")
    );

    private static final Snippet SCHEDULE_POLL_REQUEST_FIELDS = requestFields(
            fieldWithPathAndConstraints("schedule_candidate_id", SchedulePollRequest.class)
                    .type(JsonFieldType.NUMBER)
                    .description("스케줄 후보 id")
    );

    private static final Snippet SCHEDULE_POLL_RESPONSE_FIELDS = responseFields(
            fieldWithPath("poll_count")
                    .type(JsonFieldType.NUMBER)
                    .description("스케줄 후보 투표 횟수")
    );

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void pollSchedule() {
        // given
        testEntityManager.persist(testUser);
        SchedulePollRequest givenSchedulePollRequest = SchedulePollRequest.builder()
                                                                          .scheduleCandidateId(1L)
                                                                          .build();
        SchedulePollResponse expectedSchedulePollResponse = SchedulePollResponse.builder()
                                                                                .pollCount(1)
                                                                                .build();

        String path = "/groups/{groupId}/schedules/{scheduleId}/poll";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", 1L)
                                                       .pathParam("scheduleId", 1L)
                                                       .body(givenSchedulePollRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .accept(ContentType.JSON)
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      SCHEDULE_POLL_PATH_PARAMETERS,
                                                      SCHEDULE_POLL_REQUEST_FIELDS,
                                                      SCHEDULE_POLL_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .post(path);

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.CREATED.value());

        then(actualResponse.as(SchedulePollResponse.class))
                // TODO: Add description
                .usingRecursiveComparison()
                .isEqualTo(expectedSchedulePollResponse);
    }

    @Test
    void cancelPollSchedule() {
        // given
        testEntityManager.persist(testUser);
        SchedulePollRequest givenSchedulePollRequest = SchedulePollRequest.builder()
                                                                          .scheduleCandidateId(1L)
                                                                          .build();
        SchedulePollResponse expectedSchedulePollResponse = SchedulePollResponse.builder()
                                                                                .pollCount(1)
                                                                                .build();

        String path = "/groups/{groupId}/schedules/{scheduleId}/poll";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", 1L)
                                                       .pathParam("scheduleId", 1L)
                                                       .body(givenSchedulePollRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .accept(ContentType.JSON)
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      SCHEDULE_POLL_PATH_PARAMETERS,
                                                      SCHEDULE_POLL_REQUEST_FIELDS,
                                                      SCHEDULE_POLL_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .delete(path);

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());

        then(actualResponse.as(SchedulePollResponse.class))
                // TODO: Add description
                .usingRecursiveComparison()
                .isEqualTo(expectedSchedulePollResponse);
    }
}
