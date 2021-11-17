package com.postsquad.scoup.web.schedule.service;

import com.postsquad.scoup.web.schedule.controller.request.ScheduleCandidateReadRequest;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponse;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponses;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import com.postsquad.scoup.web.schedule.repository.ScheduleCandidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(classes = ScheduleCandidateService.class)
class ScheduleCandidateServiceTest {

    @MockBean
    ScheduleCandidateRepository scheduleCandidateRepository;

    ScheduleCandidateService scheduleCandidateService;

    @BeforeEach
    void setUp() {
        scheduleCandidateService = new ScheduleCandidateService(scheduleCandidateRepository);
    }

    @ParameterizedTest
    @MethodSource("readAllProvider")
    void readAll(
            ScheduleCandidateReadRequest givenScheduleCandidateReadRequest,
            List<ScheduleCandidate> givenScheduleCandidate,
            ScheduleCandidateReadAllResponses expectedScheduleCandidateReadAllResponses
    ) {

        // given
        long groupId = 1L;
        BDDMockito.given(scheduleCandidateRepository.findAllByDateTimeIncluding(
                groupId,
                givenScheduleCandidateReadRequest.getStartDate().atStartOfDay(),
                givenScheduleCandidateReadRequest.getEndDate().atStartOfDay()
        )).willReturn(givenScheduleCandidate);

        // when
        ScheduleCandidateReadAllResponses actualScheduleCandidateResponses = scheduleCandidateService.readAll(groupId, givenScheduleCandidateReadRequest);

        // then
        then(actualScheduleCandidateResponses)
                .usingRecursiveComparison()
                .isEqualTo(expectedScheduleCandidateReadAllResponses);
    }

    static Stream<Arguments> readAllProvider() {
        return Stream.of(
                Arguments.arguments(
                        ScheduleCandidateReadRequest.builder()
                                                    .startDate(LocalDate.of(2021, 9, 8))
                                                    .endDate(LocalDate.of(2021, 9, 10))
                                                    .build(),
                        List.of(
                                ScheduleCandidate.builder()
                                                 .schedule(Schedule.builder()
                                                                   .title("title")
                                                                   .description("description")
                                                                   .dueDateTime(LocalDateTime.of(2021, 9, 8, 0, 0))
                                                                   .colorCode("#00ff0000")
                                                                   .scheduleCandidates(Collections.emptyList())
                                                                   .build())
                                                 .startDateTime(LocalDateTime.of(2021, 9, 8, 0, 0))
                                                 .endDateTime(LocalDateTime.of(2021, 9, 9, 0, 0))
                                                 .build(),
                                ScheduleCandidate.builder()
                                                 .schedule(Schedule.builder()
                                                                   .title("title")
                                                                   .description("description")
                                                                   .dueDateTime(LocalDateTime.of(2021, 9, 8, 0, 0))
                                                                   .colorCode("#00ff0000")
                                                                   .scheduleCandidates(Collections.emptyList())
                                                                   .build())
                                                 .startDateTime(LocalDateTime.of(2021, 9, 8, 0, 0))
                                                 .endDateTime(LocalDateTime.of(2021, 9, 9, 0, 0))
                                                 .build(),
                                ScheduleCandidate.builder()
                                                 .schedule(Schedule.builder()
                                                                   .title("title")
                                                                   .description("description")
                                                                   .dueDateTime(LocalDateTime.of(2021, 9, 8, 0, 0))
                                                                   .colorCode("#00ff0000")
                                                                   .scheduleCandidates(Collections.emptyList())
                                                                   .build())
                                                 .startDateTime(LocalDateTime.of(2021, 9, 9, 0, 0))
                                                 .endDateTime(LocalDateTime.of(2021, 9, 10, 0, 0))
                                                 .build()
                        ),
                        ScheduleCandidateReadAllResponses.from(
                                Map.of(
                                        LocalDate.of(2021, 9, 8),
                                        List.of(
                                                ScheduleCandidateReadAllResponse.builder()
                                                                                .startDateTime(LocalDateTime.of(2021, 9, 8, 0, 0))
                                                                                .endDateTime(LocalDateTime.of(2021, 9, 9, 0, 0))
                                                                                .isConfirmed(false)
                                                                                .scheduleTitle("title")
                                                                                .scheduleDescription("description")
                                                                                .colorCode("#00ff0000")
                                                                                .build(),
                                                ScheduleCandidateReadAllResponse.builder()
                                                                                .startDateTime(LocalDateTime.of(2021, 9, 8, 0, 0))
                                                                                .endDateTime(LocalDateTime.of(2021, 9, 9, 0, 0))
                                                                                .isConfirmed(false)
                                                                                .scheduleTitle("title")
                                                                                .scheduleDescription("description")
                                                                                .colorCode("#00ff0000")
                                                                                .build()
                                        ),
                                        LocalDate.of(2021, 9, 9),
                                        List.of(
                                                ScheduleCandidateReadAllResponse.builder()
                                                                                .startDateTime(LocalDateTime.of(2021, 9, 9, 0, 0))
                                                                                .endDateTime(LocalDateTime.of(2021, 9, 10, 0, 0))
                                                                                .isConfirmed(false)
                                                                                .scheduleTitle("title")
                                                                                .scheduleDescription("description")
                                                                                .colorCode("#00ff0000")
                                                                                .build()
                                        )
                                )
                        )
                )
        );
    }
}
