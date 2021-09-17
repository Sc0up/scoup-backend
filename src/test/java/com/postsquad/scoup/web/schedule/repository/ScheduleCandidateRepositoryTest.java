package com.postsquad.scoup.web.schedule.repository;

import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ScheduleCandidateRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ScheduleCandidateRepository scheduleCandidateRepository;

    @ParameterizedTest
    @MethodSource("findAllByDateTimeIncludingProvider")
    void findAllByDateTimeInclude(
            String description,
            List<Schedule> givenSchedules,
            LocalDate givenStartDate,
            LocalDate givenEndDate,
            List<ScheduleCandidate> expectedScheduleCandidates
    ) {
        // given
        for (Schedule each : givenSchedules) {
            entityManager.persist(each);
        }

        // when
        List<ScheduleCandidate> actualScheduleCandidates = scheduleCandidateRepository.findAllByDateTimeIncluding(givenStartDate.atStartOfDay(), givenEndDate.atStartOfDay());

        // then
        then(actualScheduleCandidates)
                .as("시간 범위: %s" + description)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdDateTime", "modifiedDateTime")
                .isEqualTo(expectedScheduleCandidates);
    }

    static Stream<Arguments> findAllByDateTimeIncludingProvider() {
        class Provider {
            ScheduleCandidate scheduleCandidateOf(LocalDateTime startDateTime, LocalDateTime endDateTime) {
                return ScheduleCandidate.builder()
                                        .isConfirmed(false)
                                        .startDateTime(startDateTime)
                                        .endDateTime(endDateTime)
                                        .build();
            }
        }

        return Stream.of(
                Arguments.arguments(
                        "범위 일치",
                        List.of(
                                Schedule.builder()
                                        .title("title")
                                        .dueDateTime(LocalDateTime.now())
                                        .scheduleCandidates(List.of(
                                                new Provider().scheduleCandidateOf(
                                                        LocalDateTime.of(2021, 9, 1, 0, 0),
                                                        LocalDateTime.of(2021, 9, 1, 23, 59)
                                                )
                                        )).build()
                        ),
                        LocalDate.of(2021, 9, 1),
                        LocalDate.of(2021, 9, 2),
                        List.of(
                                new Provider().scheduleCandidateOf(
                                        LocalDateTime.of(2021, 9, 1, 0, 0),
                                        LocalDateTime.of(2021, 9, 1, 23, 59)
                                )
                        )
                ), Arguments.arguments(
                        "범위 안쪽",
                        List.of(
                                Schedule.builder()
                                        .title("title")
                                        .dueDateTime(LocalDateTime.now())
                                        .scheduleCandidates(List.of(
                                                new Provider().scheduleCandidateOf(
                                                        LocalDateTime.of(2021, 9, 1, 1, 0),
                                                        LocalDateTime.of(2021, 9, 1, 22, 59)
                                                )
                                        )).build()
                        ),
                        LocalDate.of(2021, 9, 1),
                        LocalDate.of(2021, 9, 2),
                        List.of(
                                new Provider().scheduleCandidateOf(
                                        LocalDateTime.of(2021, 9, 1, 1, 0),
                                        LocalDateTime.of(2021, 9, 1, 22, 59)
                                )
                        )
                ), Arguments.arguments(
                        "범위 안쪽이면서 왼쪽으로 벗어나게",
                        List.of(
                                Schedule.builder()
                                        .title("title")
                                        .dueDateTime(LocalDateTime.now())
                                        .scheduleCandidates(List.of(
                                                new Provider().scheduleCandidateOf(
                                                        LocalDateTime.of(2021, 8, 31, 23, 59),
                                                        LocalDateTime.of(2021, 9, 1, 23, 59)
                                                )
                                        )).build()
                        ),
                        LocalDate.of(2021, 9, 1),
                        LocalDate.of(2021, 9, 2),
                        List.of(
                                new Provider().scheduleCandidateOf(
                                        LocalDateTime.of(2021, 8, 31, 23, 59),
                                        LocalDateTime.of(2021, 9, 1, 23, 59)
                                )
                        )
                ), Arguments.arguments(
                        "범위 안쪽이면서 오른쪽으로 벗어나게",
                        List.of(
                                Schedule.builder()
                                        .title("title")
                                        .dueDateTime(LocalDateTime.now())
                                        .scheduleCandidates(List.of(
                                                new Provider().scheduleCandidateOf(
                                                        LocalDateTime.of(2021, 9, 1, 0, 0),
                                                        LocalDateTime.of(2021, 9, 2, 0, 0)
                                                )
                                        )).build()
                        ),
                        LocalDate.of(2021, 9, 1),
                        LocalDate.of(2021, 9, 2),
                        List.of(
                                new Provider().scheduleCandidateOf(
                                        LocalDateTime.of(2021, 9, 1, 0, 0),
                                        LocalDateTime.of(2021, 9, 2, 0, 0)
                                )
                        )
                ), Arguments.arguments(
                        "범위 왼쪽에 걸치게",
                        List.of(
                                Schedule.builder()
                                        .title("title")
                                        .dueDateTime(LocalDateTime.now())
                                        .scheduleCandidates(List.of(
                                                new Provider().scheduleCandidateOf(
                                                        LocalDateTime.of(2021, 8, 31, 23, 59),
                                                        LocalDateTime.of(2021, 9, 1, 0, 1)
                                                )
                                        )).build()
                        ),
                        LocalDate.of(2021, 9, 1),
                        LocalDate.of(2021, 9, 2),
                        List.of(
                                new Provider().scheduleCandidateOf(
                                        LocalDateTime.of(2021, 8, 31, 23, 59),
                                        LocalDateTime.of(2021, 9, 1, 0, 1)
                                )
                        )
                ), Arguments.arguments(
                        "범위 왼쪽 벗어남",
                        List.of(
                                Schedule.builder()
                                        .title("title")
                                        .dueDateTime(LocalDateTime.now())
                                        .scheduleCandidates(List.of(
                                                new Provider().scheduleCandidateOf(
                                                        LocalDateTime.of(2021, 8, 31, 23, 59),
                                                        LocalDateTime.of(2021, 9, 1, 0, 0)
                                                )
                                        )).build()
                        ),
                        LocalDate.of(2021, 9, 1),
                        LocalDate.of(2021, 9, 2),
                        Collections.emptyList()
                ), Arguments.arguments(
                        "범위 오른쪽에 걸치게",
                        List.of(
                                Schedule.builder()
                                        .title("title")
                                        .dueDateTime(LocalDateTime.now())
                                        .scheduleCandidates(List.of(
                                                new Provider().scheduleCandidateOf(
                                                        LocalDateTime.of(2021, 9, 1, 23, 59),
                                                        LocalDateTime.of(2021, 9, 2, 0, 0)
                                                )
                                        )).build()
                        ),
                        LocalDate.of(2021, 9, 1),
                        LocalDate.of(2021, 9, 2),
                        List.of(
                                new Provider().scheduleCandidateOf(
                                        LocalDateTime.of(2021, 9, 1, 23, 59),
                                        LocalDateTime.of(2021, 9, 2, 0, 0)
                                )
                        )
                ), Arguments.arguments(
                        "범위 오른쪽 벗어남",
                        List.of(
                                Schedule.builder()
                                        .title("title")
                                        .dueDateTime(LocalDateTime.now())
                                        .scheduleCandidates(List.of(
                                                new Provider().scheduleCandidateOf(
                                                        LocalDateTime.of(2021, 9, 2, 0, 0),
                                                        LocalDateTime.of(2021, 9, 2, 0, 1)
                                                )
                                        )).build()
                        ),
                        LocalDate.of(2021, 9, 1),
                        LocalDate.of(2021, 9, 2),
                        Collections.emptyList()
                ), Arguments.arguments(
                        "범위 둘러쌈",
                        List.of(
                                Schedule.builder()
                                        .title("title")
                                        .dueDateTime(LocalDateTime.now())
                                        .scheduleCandidates(List.of(
                                                new Provider().scheduleCandidateOf(
                                                        LocalDateTime.of(2021, 8, 31, 23, 59),
                                                        LocalDateTime.of(2021, 9, 2, 0, 1)
                                                )
                                        )).build()
                        ),
                        LocalDate.of(2021, 9, 1),
                        LocalDate.of(2021, 9, 2),
                        List.of(
                                new Provider().scheduleCandidateOf(
                                        LocalDateTime.of(2021, 8, 31, 23, 59),
                                        LocalDateTime.of(2021, 9, 2, 0, 1)
                                )
                        )
                )
        );
    }
}
