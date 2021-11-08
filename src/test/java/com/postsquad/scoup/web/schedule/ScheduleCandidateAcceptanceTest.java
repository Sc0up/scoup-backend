package com.postsquad.scoup.web.schedule;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.TestEntityManager;
import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCandidateReadRequest;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponse;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponses;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

class ScheduleCandidateAcceptanceTest extends AcceptanceTestBase {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    // temporary token with sub(userId) = 1, exp = 2023-01-01T00:00:00.000(Korean Standard Time)
    private static String TEST_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcyNDk4ODAwfQ.DXojMeUGIq77XWvQK0luZtZhsi-c6s9qjiiu9vHhkbg";
    private User testUser = User.builder()
                                .nickname("nickname")
                                .email("email@email.com")
                                .password("password")
                                .avatarUrl("url")
                                .username("username")
                                .oAuthUsers(List.of(OAuthUser.of(OAuthType.NONE, "")))
                                .build();

    @BeforeEach
    void setUp() {
        userRepository.save(testUser);
    }

    @ParameterizedTest
    @MethodSource("readAllProvider")
    void readAll(
            String description,
            List<Schedule> givenSchedules,
            ScheduleCandidateReadRequest givenScheduleCandidateReadRequest,
            ScheduleCandidateReadAllResponses expectedScheduleCandidateReadAllResponses
    ) {
        // given
        Group group = Group.builder()
                           .name("group")
                           .build();
        group.addSchedules(givenSchedules);
        for (Schedule schedule : givenSchedules) {
            schedule.setGroup(group);
            for (ScheduleCandidate scheduleCandidate : schedule.getScheduleCandidates()) {
                scheduleCandidate.setSchedule(schedule);
            }
        }

        testEntityManager.persist(group);


        // TODO: 그룹 연결 후에는 그룹도 넣어줘야함.
        String path = "/api/groups/1/schedule-candidates";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .header("Authorization", TEST_TOKEN)
                                                       .queryParam("start_date", givenScheduleCandidateReadRequest.getStartDate().toString())
                                                       .queryParam("end_date", givenScheduleCandidateReadRequest.getEndDate().toString());

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
                .ignoringFields("scheduleCandidateResponses.id")
                .ignoringFields("scheduleCandidateResponses.scheduleId")
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
                                                    .startDate(LocalDate.of(2021, 9, 1))
                                                    .endDate(LocalDate.of(2021, 9, 2))
                                                    .build(),
                        ScheduleCandidateReadAllResponses.from(
                                Map.of(
                                        LocalDate.of(2021, 9, 1),
                                        Arrays.asList(
                                                ScheduleCandidateReadAllResponse.builder()
                                                                                .startDateTime(readAllTestData.scheduleCandidate1().getStartDateTime())
                                                                                .endDateTime(readAllTestData.scheduleCandidate1().getEndDateTime())
                                                                                .isConfirmed(false)
                                                                                .scheduleTitle("title")
                                                                                .colorCode("#00ff0000")
                                                                                .build()
                                        ))
                        )
                )
        );
    }
}
