package com.postsquad.scoup.web.schedule;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.TestEntityManager;
import com.postsquad.scoup.web.schedule.controller.response.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class ScheduleAcceptanceTest extends AcceptanceTestBase {

    private static final Snippet SCHEDULE_READ_ONE_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID"),
            parameterWithName("scheduleId")
                    .description("일정 ID")
    );

    private static final Snippet SCHEDULE_READ_ONE_RESPONSE_FIELDS = responseFields(
            fieldWithPath("id")
                    .type(JsonFieldType.NUMBER)
                    .description("일정 id"),
            fieldWithPath("title")
                    .type(JsonFieldType.STRING)
                    .description("일정 제목"),
            fieldWithPath("description")
                    .type(JsonFieldType.STRING)
                    .description("일정 설명"),
            fieldWithPath("poll_due_date_time")
                    .type(JsonFieldType.STRING)
                    .description("일정 투표 기한"),
            fieldWithPath("confirmed_schedule")
                    .type(JsonFieldType.OBJECT)
                    .description("확정된 일정"),
            fieldWithPath("confirmed_schedule.id")
                    .type(JsonFieldType.NUMBER)
                    .description("확정된 일정 id"),
            fieldWithPath("confirmed_schedule.start_date_time")
                    .type(JsonFieldType.STRING)
                    .description("확정된 일정 시작 시간"),
            fieldWithPath("confirmed_schedule.end_date_time")
                    .type(JsonFieldType.STRING)
                    .description("확정된 일정 종료 시간"),
            fieldWithPath("confirmed_schedule.confirmed_participants[]")
                    .type(JsonFieldType.ARRAY)
                    .description("확정된 일정 참가자 목록"),
            fieldWithPath("confirmed_schedule.confirmed_participants[].nickname")
                    .type(JsonFieldType.STRING)
                    .description("확정된 일정 참가자 별명"),
            fieldWithPath("confirmed_schedule.confirmed_participants[].username")
                    .type(JsonFieldType.STRING)
                    .description("확정된 일정 참가자 이름"),
            fieldWithPath("schedule_candidates[]")
                    .type(JsonFieldType.ARRAY)
                    .description("일정 후보 목록"),
            fieldWithPath("schedule_candidates[].id")
                    .type(JsonFieldType.NUMBER)
                    .description("일정 후보 id"),
            fieldWithPath("schedule_candidates[].start_date_time")
                    .type(JsonFieldType.STRING)
                    .description("일정 후보 시작 시간"),
            fieldWithPath("schedule_candidates[].end_date_time")
                    .type(JsonFieldType.STRING)
                    .description("일정 후보 종료 시간"),
            fieldWithPath("schedule_candidates[].poll_count")
                    .type(JsonFieldType.NUMBER)
                    .description("일정 후보 투표 개수"),
            fieldWithPath("schedule_candidates[].confirmed_participants[]")
                    .type(JsonFieldType.ARRAY)
                    .description("일정 후보 참가자 목록"),
            fieldWithPath("schedule_candidates[].confirmed_participants[].nickname")
                    .type(JsonFieldType.STRING)
                    .description("일정 후보 참가자 별명"),
            fieldWithPath("schedule_candidates[].confirmed_participants[].username")
                    .type(JsonFieldType.STRING)
                    .description("일정 후보 참가자 이름")
    );

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void readOne() {
        //given
        testEntityManager.persist(testUser);

        ScheduleReadOneResponse expectedScheduleReadOneResponse = ScheduleReadOneResponse.builder()
                                                                                         .id(1L)
                                                                                         .title("schedule title")
                                                                                         .description("schedule description")
                                                                                         .pollDueDateTime(LocalDateTime.of(2021, 11, 24, 0, 0))
                                                                                         .confirmedSchedule(
                                                                                                 ConfirmedScheduleResponseForReadOneSchedule.builder()
                                                                                                                                            .id(1L)
                                                                                                                                            .startDateTime(LocalDateTime.of(2021, 11, 22, 0, 0))
                                                                                                                                            .endDateTime(LocalDateTime.of(2021, 11, 23, 0, 0))
                                                                                                                                            .confirmedParticipants(List.of(
                                                                                                                                                    ConfirmedParticipantResponse.builder()
                                                                                                                                                                                .nickname("nickname")
                                                                                                                                                                                .username("username")
                                                                                                                                                                                .build()
                                                                                                                                            ))
                                                                                                                                            .build()
                                                                                         )
                                                                                         .scheduleCandidates(List.of(
                                                                                                 ScheduleCandidateResponseForReadOneSchedule.builder()
                                                                                                                                            .id(1L)
                                                                                                                                            .startDateTime(LocalDateTime.of(2021, 11, 22, 0, 0))
                                                                                                                                            .endDateTime(LocalDateTime.of(2021, 11, 23, 0, 0))
                                                                                                                                            .pollCount(1)
                                                                                                                                            .confirmedParticipants(List.of(
                                                                                                                                                    ConfirmedParticipantResponse.builder()
                                                                                                                                                                                .nickname("nickname")
                                                                                                                                                                                .username("username")
                                                                                                                                                                                .build()
                                                                                                                                            ))
                                                                                                                                            .build()
                                                                                         ))
                                                                                         .build();

        String path = "/groups/{groupId}/schedules/{scheduleId}";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", 1L)
                                                       .pathParam("scheduleId", 1L);
        //when
        Response actualResponse = givenRequest.when()
                                              .accept(ContentType.JSON)
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      SCHEDULE_READ_ONE_PATH_PARAMETERS,
                                                      SCHEDULE_READ_ONE_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .get(path);

        //then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());

        then(actualResponse.as(ScheduleReadOneResponse.class))
                // TODO: Add description
                .usingRecursiveComparison()
                .isEqualTo(expectedScheduleReadOneResponse);
    }
}
