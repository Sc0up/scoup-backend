package com.postsquad.scoup.web.schedule;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.TestEntityManager;
import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleReadAllResponses;
import com.postsquad.scoup.web.schedule.domain.ConfirmedSchedule;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.provider.ConfirmedScheduleReadAllProvider;
import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

class ConfirmedScheduleAcceptanceTest extends AcceptanceTestBase {

    @Autowired
    TestEntityManager testEntityManager;

    protected User testUser2 = User.builder()
                                  .nickname("nickname2")
                                  .email("email2@email.com")
                                  .password("password2")
                                  .avatarUrl("url2")
                                  .username("username2")
                                  .oAuthUsers(List.of(OAuthUser.of(OAuthType.NONE, "")))
                                  .build();

    @ParameterizedTest
    @ArgumentsSource(ConfirmedScheduleReadAllProvider.class)
    @DisplayName("확정된 일정을 조회할 수 있다")
    void readAllConfirmedSchedules(String description, Long givenGroupId,
                                   ConfirmedScheduleReadAllResponses expectedConfirmedScheduleReadAllResponses) {
        // given
        testEntityManager.persist(testUser);
        testEntityManager.persist(testUser2);

        Group group = Group.builder()
                           .name("name")
                           .description("")
                           .schedules(new ArrayList<>())
                           .owner(testUser)
                           .build();

        Schedule schedule = Schedule.builder()
                                    .group(group)
                                    .title("schedule title")
                                    .description("schedule description")
                                    .dueDateTime(LocalDateTime.of(2021, 9, 23, 0, 0))
                                    .build();
        group.addSchedule(schedule);

        ConfirmedSchedule confirmedSchedule = ConfirmedSchedule.builder()
                                                               .startDateTime(LocalDateTime.of(2021, 9, 25, 9, 0))
                                                               .endDateTime(LocalDateTime.of(2021, 9, 25, 11, 0))
                                                               .confirmedParticipants(List.of(testUser, testUser2))
                                                               .build();
        schedule.confirmSchedule(confirmedSchedule);
        testEntityManager.persist(group);
        String path = "/api/groups/{groupId}/confirmed-schedules";

        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .pathParam("groupId", givenGroupId)
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
