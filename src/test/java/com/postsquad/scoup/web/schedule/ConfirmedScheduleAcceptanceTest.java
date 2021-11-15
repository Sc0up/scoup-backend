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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

class ConfirmedScheduleAcceptanceTest extends AcceptanceTestBase {

    private static final Snippet CONFIRMED_SCHEDULE_READ_ALL_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID")
    );

    private static final Snippet CONFIRMED_SCHEDULE_READ_ALL_RESPONSE_FIELDS = responseFields(
            fieldWithPath("confirmed_schedules[]")
                    .type(JsonFieldType.ARRAY)
                    .description("확정된 스케줄 목록"),
            fieldWithPath("confirmed_schedules[].title")
                    .type(JsonFieldType.STRING)
                    .description("확정된 스케줄 시작 제목"),
            fieldWithPath("confirmed_schedules[].description")
                    .type(JsonFieldType.STRING)
                    .description("확정된 스케줄 시작 설명")
                    .optional(),
            fieldWithPath("confirmed_schedules[].start_date_time")
                    .type(JsonFieldType.STRING)
                    .description("확정된 스케줄 시작 시간"),
            fieldWithPath("confirmed_schedules[].end_date_time")
                    .type(JsonFieldType.STRING)
                    .description("확정된 스케줄 종료 시간"),
            fieldWithPath("confirmed_schedules[].confirmed_participants[]")
                    .type(JsonFieldType.ARRAY)
                    .description("확정된 스케줄의 참가자 목록"),
            fieldWithPath("confirmed_schedules[].confirmed_participants[].nickname")
                    .type(JsonFieldType.STRING)
                    .description("확정된 스케줄의 참가자 닉네임"),
            fieldWithPath("confirmed_schedules[].confirmed_participants[].username")
                    .type(JsonFieldType.STRING)
                    .description("확정된 스케줄의 참가자 이름")
    );

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
        String path = "/groups/{groupId}/confirmed-schedules";

        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .pathParam("groupId", givenGroupId)
                                                       .contentType(ContentType.JSON)
                                                       .header(AUTHORIZATION, TEST_TOKEN);

        // when
        Response actualResponse = givenRequest.when()
                                              .accept(ContentType.JSON)
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      CONFIRMED_SCHEDULE_READ_ALL_PATH_PARAMETERS,
                                                      CONFIRMED_SCHEDULE_READ_ALL_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .get(path);

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
