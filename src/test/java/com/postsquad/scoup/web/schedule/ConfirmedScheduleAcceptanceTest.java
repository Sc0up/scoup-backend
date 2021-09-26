package com.postsquad.scoup.web.schedule;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleReadAllResponses;
import com.postsquad.scoup.web.schedule.provider.ConfirmedScheduleReadAllProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

class ConfirmedScheduleAcceptanceTest extends AcceptanceTestBase {

    @Autowired
    ConfirmedScheduleTestDataSetup confirmedScheduleTestDataSetup;

    @ParameterizedTest
    @ArgumentsSource(ConfirmedScheduleReadAllProvider.class)
    @DisplayName("확정된 일정을 조회할 수 있다")
    void readAllConfirmedSchedules(String description, Long givenGroupId,
                                   ConfirmedScheduleReadAllResponses expectedConfirmedScheduleReadAllResponses) {
        // given
        confirmedScheduleTestDataSetup.execute();
        String path = "/api/groups/{groupId}/confirmed-schedules";

        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .pathParam("groupId", 1L)
                                                       .contentType(ContentType.JSON)
                                                       .header(AUTHORIZATION, TEST_TOKEN);

        // when
        Response actualResponse = givenRequest.when()
                                              .accept(ContentType.JSON)
                                              .log().all()
                                              .get();

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());

        then(actualResponse.as(ConfirmedScheduleReadAllResponses.class))
                .as("확정 일정 조회 : %s", description)
                .usingRecursiveComparison()
                .isEqualTo(expectedConfirmedScheduleReadAllResponses);
    }
}
