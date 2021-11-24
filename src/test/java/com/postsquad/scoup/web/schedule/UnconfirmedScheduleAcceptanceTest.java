package com.postsquad.scoup.web.schedule;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.TestEntityManager;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedParticipantResponse;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateResponseForUnconfirmedSchedule;
import com.postsquad.scoup.web.schedule.controller.response.UnconfirmedScheduleReadAllResponse;
import com.postsquad.scoup.web.schedule.controller.response.UnconfirmedScheduleReadAllResponses;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

class UnconfirmedScheduleAcceptanceTest extends AcceptanceTestBase {

    private static final Snippet UNCONFIRMED_SCHEDULE_READ_ALL_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID")
    );

    private static final Snippet UNCONFIRMED_SCHEDULE_READ_ALL_RESPONSE_FIELDS = responseFields(
            fieldWithPath("unconfirmed_schedules[]")
                    .type(JsonFieldType.ARRAY)
                    .description("미확정 일정 목록"),
            fieldWithPath("unconfirmed_schedules[].title")
                    .type(JsonFieldType.STRING)
                    .description("미확정 일정 제목"),
            fieldWithPath("unconfirmed_schedules[].description")
                    .type(JsonFieldType.STRING)
                    .description("미확정 일정 설명")
                    .optional(),
            fieldWithPath("unconfirmed_schedules[].schedule_candidates[]")
                    .type(JsonFieldType.ARRAY)
                    .description("미확정 일정 후보"),
            fieldWithPath("unconfirmed_schedules[].schedule_candidates[].candidate_id")
                    .type(JsonFieldType.NUMBER)
                    .description("미확정 일정 후보 id"),
            fieldWithPath("unconfirmed_schedules[].schedule_candidates[].start_date_time")
                    .type(JsonFieldType.STRING)
                    .description("미확정 일정 후보 시작 시간"),
            fieldWithPath("unconfirmed_schedules[].schedule_candidates[].end_date_time")
                    .type(JsonFieldType.STRING)
                    .description("미확정 일정 후보 종료 시간"),
            fieldWithPath("unconfirmed_schedules[].schedule_candidates[].due_date_time")
                    .type(JsonFieldType.STRING)
                    .description("미확정 일정 후보 투표 기한"),
            fieldWithPath("unconfirmed_schedules[].schedule_candidates[].confirmed_participants[]")
                    .type(JsonFieldType.ARRAY)
                    .description("미확정 일정 후보의 확정된 참가자 목록"),
            fieldWithPath("unconfirmed_schedules[].schedule_candidates[].confirmed_participants[].nickname")
                    .type(JsonFieldType.STRING)
                    .description("미확정 일정 후보의 확정된 참가자 닉네임"),
            fieldWithPath("unconfirmed_schedules[].schedule_candidates[].confirmed_participants[].username")
                    .type(JsonFieldType.STRING)
                    .description("미확정 일정 후보의 확정된 참가자 이름")
    );

    @Autowired
    TestEntityManager testEntityManager;


    @Test
    @DisplayName("미확정된 일정을 조회할 수 있다")
    void readAll() {
        // given
        testEntityManager.persist(testUser);
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
        ScheduleCandidate scheduleCandidate = ScheduleCandidate.builder()
                                                               .startDateTime(LocalDateTime.of(2021, 9, 21, 0, 0))
                                                               .endDateTime(LocalDateTime.of(2021, 9, 22, 0, 0))
                                                               .build();
        schedule.addScheduleCandidate(scheduleCandidate);

        testEntityManager.persist(group);

        UnconfirmedScheduleReadAllResponses expectedUnconfirmedScheduleReadAllResponses = UnconfirmedScheduleReadAllResponses.of(List.of(
                UnconfirmedScheduleReadAllResponse.builder()
                                                  .title("schedule title")
                                                  .description("schedule description")
                                                  .scheduleCandidates(List.of(
                                                          ScheduleCandidateResponseForUnconfirmedSchedule.builder()
                                                                                                         .candidateId(1L)
                                                                                                         .startDateTime(LocalDateTime.of(2021, 9, 21, 0, 0))
                                                                                                         .endDateTime(LocalDateTime.of(2021, 9, 22, 0, 0))
                                                                                                         .dueDateTime(LocalDateTime.of(2021, 9, 23, 0, 0))
                                                                                                         .confirmedParticipants(List.of(
                                                                                                                 ConfirmedParticipantResponse.builder()
                                                                                                                                             .nickname("nickname")
                                                                                                                                             .username("username")
                                                                                                                                             .build()
                                                                                                         ))
                                                                                                         .build()
                                                  ))
                                                  .build()
        ));

        String path = "/groups/{groupId}/unconfirmed-schedules";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .pathParam("groupId", group.getId())
                                                       .contentType(ContentType.JSON)
                                                       .header(AUTHORIZATION, TEST_TOKEN);

        // when
        Response actualResponse = givenRequest.when()
                                              .accept(ContentType.JSON)
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      UNCONFIRMED_SCHEDULE_READ_ALL_PATH_PARAMETERS,
                                                      UNCONFIRMED_SCHEDULE_READ_ALL_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .get(path);

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());

        then(actualResponse.as(UnconfirmedScheduleReadAllResponses.class))
                // TODO: Add description
                .usingRecursiveComparison()
                .isEqualTo(expectedUnconfirmedScheduleReadAllResponses);
    }
}
