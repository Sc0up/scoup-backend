package com.postsquad.scoup.web.schedule;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.TestEntityManager;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCandidateCreationRequest;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleConfirmationRequest;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCreationRequest;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleModificationRequest;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedParticipantResponse;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleResponseForReadOneSchedule;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateResponseForReadOneSchedule;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleReadOneResponse;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.provider.ValidateScheduleCreationRequestProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

    private static final Snippet SCHEDULE_CREATION_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID")
    );

    private static final Snippet SCHEDULE_CREATION_REQUEST_FIELDS = requestFields(
            fieldWithPathAndConstraints("title", ScheduleCreationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("일정 제목"),
            fieldWithPathAndConstraints("description", ScheduleCreationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("일정 설명"),
            fieldWithPathAndConstraints("poll_due_date_time", ScheduleCreationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("일정 투표 마감 기한"),
            fieldWithPathAndConstraints("is_poll_anonymous", ScheduleCreationRequest.class)
                    .type(JsonFieldType.BOOLEAN)
                    .description("익명 투표 여부"),
            fieldWithPathAndConstraints("schedule_candidates[]", ScheduleCreationRequest.class)
                    .type(JsonFieldType.ARRAY)
                    .description("일정 후보 목록"),
            fieldWithPathAndConstraints("schedule_candidates[].start_date_time", ScheduleCreationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("일정 후보 시작 시간"),
            fieldWithPathAndConstraints("schedule_candidates[].end_date_time", ScheduleCreationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("일정 후보 종료 시간")
    );

    private static final Snippet SCHEDULE_UPDATE_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID"),
            parameterWithName("scheduleId")
                    .description("일정 ID")
    );

    private static final Snippet SCHEDULE_UPDATE_REQUEST_FIELDS = requestFields(
            fieldWithPathAndConstraints("title", ScheduleModificationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("수정할 일정 제목"),
            fieldWithPathAndConstraints("description", ScheduleModificationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("수정할 일정 설명")
    );

    private static final Snippet SCHEDULE_CONFIRMATION_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID"),
            parameterWithName("scheduleId")
                    .description("일정 ID")
    );

    private static final Snippet SCHEDULE_CONFIRMATION_REQUEST_FIELDS = requestFields(
            fieldWithPathAndConstraints("schedule_candidate_id", ScheduleConfirmationRequest.class)
                    .type(JsonFieldType.NUMBER)
                    .description("확정할 일정 후보 id")
    );

    private static final Snippet SCHEDULE_DELETION_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID"),
            parameterWithName("scheduleId")
                    .description("일정 ID")
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

    @Test
    void create() {
        //given
        testEntityManager.persist(testUser);
        Group givenGroup = Group.builder()
                                .name("name")
                                .build();

        testEntityManager.persist(givenGroup);

        ScheduleCreationRequest givenScheduleCreationRequest = ScheduleCreationRequest.builder()
                                                                                      .title("title")
                                                                                      .description("description")
                                                                                      .pollDueDateTime(LocalDateTime.of(21, 11, 27, 0, 0))
                                                                                      .isPollAnonymous(true)
                                                                                      .scheduleCandidates(List.of(
                                                                                              ScheduleCandidateCreationRequest.builder()
                                                                                                                              .startDateTime(LocalDateTime.of(21, 11, 25, 0, 0))
                                                                                                                              .endDateTime(LocalDateTime.of(21, 11, 26, 0, 0))
                                                                                                                              .build()
                                                                                      ))
                                                                                      .build();

        String path = "/groups/{groupId}/schedules";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", givenGroup.getId())
                                                       .body(givenScheduleCreationRequest);
        //when
        Response actualResponse = givenRequest.when()
                                              .accept(ContentType.JSON)
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      SCHEDULE_CREATION_PATH_PARAMETERS,
                                                      SCHEDULE_CREATION_REQUEST_FIELDS,
                                                      DEFAULT_POST_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .post(path);

        //then
        long actualId = actualResponse.then()
                                      .log().all()
                                      .statusCode(HttpStatus.CREATED.value())
                                      .extract()
                                      .jsonPath()
                                      .getInt("id");

        testEntityManager.findAndConsume(Schedule.class, actualId, schedule -> {
            assertThat(schedule).hasFieldOrPropertyWithValue("title", "title")
                                .hasFieldOrPropertyWithValue("description", "description");
        });
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateScheduleCreationRequestProvider.class)
    void validateScheduleCreateRequest(String description, ScheduleCreationRequest givenScheduleCreationRequest, ErrorResponse expectedResponse) {
        // given
        testEntityManager.persist(testUser);
        Group givenGroup = Group.builder()
                                .name("name")
                                .build();

        testEntityManager.persist(givenGroup);

        String path = "/groups/{groupId}/schedules";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", givenGroup.getId())
                                                       .body(givenScheduleCreationRequest);

        //when
        Response actualResponse = givenRequest.when()
                                              .accept(ContentType.JSON)
                                              .log().all()
                                              .post(path);

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.BAD_REQUEST.value());
        then(actualResponse.as(ErrorResponse.class))
                .as(description)
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForErrorResponse)
                .isEqualTo(expectedResponse);
    }

    @Test
    void update() {
        //given
        testEntityManager.persist(testUser);
        ScheduleModificationRequest scheduleConfirmationRequest = ScheduleModificationRequest.builder()
                                                                                             .title("title")
                                                                                             .description("description")
                                                                                             .build();

        String path = "/groups/{groupId}/schedules/{scheduleId}";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", 1L)
                                                       .pathParam("scheduleId", 1L)
                                                       .body(scheduleConfirmationRequest);

        //when
        Response actualResponse = givenRequest.when()
                                              .accept(ContentType.JSON)
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      SCHEDULE_UPDATE_PATH_PARAMETERS,
                                                      SCHEDULE_UPDATE_REQUEST_FIELDS
                                              ))
                                              .log().all()
                                              .patch(path);

        //then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.NO_CONTENT.value());

        // TODO: id로 db 조회하여 검증
    }

    @Test
    void delete() {
        //given
        testEntityManager.persist(testUser);
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
                                                      SCHEDULE_DELETION_PATH_PARAMETERS
                                              ))
                                              .log().all()
                                              .delete(path);

        //then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.NO_CONTENT.value());
        // TODO: id로 db 조회하여 검증
    }

    @Test
    void confirmSchedule() {
        //given
        testEntityManager.persist(testUser);
        ScheduleConfirmationRequest scheduleConfirmationRequest = ScheduleConfirmationRequest.builder()
                                                                                             .scheduleCandidateId(1L)
                                                                                             .build();

        String path = "/groups/{groupId}/schedules/{scheduleId}/confirm";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", 1L)
                                                       .pathParam("scheduleId", 1L)
                                                       .body(scheduleConfirmationRequest);

        //when
        Response actualResponse = givenRequest.when()
                                              .accept(ContentType.JSON)
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      SCHEDULE_CONFIRMATION_PATH_PARAMETERS,
                                                      SCHEDULE_CONFIRMATION_REQUEST_FIELDS
                                              ))
                                              .log().all()
                                              .patch(path);

        //then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.NO_CONTENT.value());

        // TODO: id로 db 조회하여 검증
    }
}
