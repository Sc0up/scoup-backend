package com.postsquad.scoup.web.schedule;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCandidateReadRequest;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponse;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponses;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

@Transactional
class ScheduleCandidateAcceptanceTest extends AcceptanceTestBase {

    @Autowired
    EntityManager entityManager;

    @ParameterizedTest
    @MethodSource("readAllProvider")
    void readAll(
            String description,
            List<Schedule> givenSchedules,
            ScheduleCandidateReadRequest givenScheduleCandidateReadRequest,
            ScheduleCandidateReadAllResponses expectedScheduleCandidateReadAllResponses
    ) {
        // given
        for (Schedule each : givenSchedules) {
            entityManager.persist(each);
        }

        // TODO: 그룹 연결 후에는 그룹도 넣어줘야함.
        String path = "/api/groups/1/schedule-candidates";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .body(givenScheduleCandidateReadRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .accept(ContentType.JSON)
                                              .log().all()
                                              .get();

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());

        then(actualResponse.as(ScheduleCandidateReadAllResponses.class))
                .as("", description)
                .usingRecursiveComparison()
                .isEqualTo(expectedScheduleCandidateReadAllResponses);

    }

    static Stream<Arguments> readAllProvider() {
        class ReadAllTestData {
            Schedule schedule1() {
                return Schedule.builder()
                               .title("title")
                               .dueDateTime(LocalDateTime.now())
                               .scheduleCandidates(Arrays.asList(
                                       scheduleCandidate1()
                               )).build();
            }

            ScheduleCandidate scheduleCandidate1() {
                return ScheduleCandidate.builder()
                                        .isConfirmed(false)
                                        .startDateTime(LocalDateTime.parse("2021-09-01T12:00"))
                                        .endDateTime(LocalDateTime.parse("2021-09-01T15:00"))
                                        .build();
            }
        }
        ReadAllTestData readAllTestData = new ReadAllTestData();
        return Stream.of(
                Arguments.of(
                        "",
                        Arrays.asList(
                                readAllTestData.schedule1()
                        ),
                        ScheduleCandidateReadRequest.builder()
                                                    .startDate(LocalDate.now())
                                                    .endDate(LocalDate.now())
                                                    .build(),
                        ScheduleCandidateReadAllResponses.from(
                                Map.of(
                                        LocalDate.now(),
                                        Arrays.asList(
                                                ScheduleCandidateReadAllResponse.builder()
                                                                                .startDateTime(readAllTestData.scheduleCandidate1().getStartDateTime())
                                                                                .endDateTime(readAllTestData.scheduleCandidate1().getEndDateTime())
                                                                                .isConfirmed(false)
                                                                                .scheduleTitle("title")
                                                                                .build()
                                        ))
                        )
                )
        );
    }
}
