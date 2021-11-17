package com.postsquad.scoup.web.schedule.repository;

import com.postsquad.scoup.web.group.domain.Group;
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
import java.util.function.Supplier;
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
        Group group = Group.builder()
                           .name("group")
                           .build();
        entityManager.persist(group);

        for (Schedule each : givenSchedules) {
            each.setGroup(group);
            entityManager.persist(each);
        }

        // when
        List<ScheduleCandidate> actualScheduleCandidates = scheduleCandidateRepository.findAllByDateTimeIncluding(group.getId(), givenStartDate.atStartOfDay(), givenEndDate.atStartOfDay());

        // then
        then(actualScheduleCandidates)
                .as("시간 범위: %s" + description)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdDateTime", "modifiedDateTime")
                .isEqualTo(expectedScheduleCandidates);
    }

    static Stream<Arguments> findAllByDateTimeIncludingProvider() {
        class Provider {
            Schedule scheduleFrom(List<ScheduleCandidate> scheduleCandidates) {
                Schedule schedule = Schedule.builder()
                                            .title("title")
                                            .dueDateTime(LocalDateTime.now())
                                            .scheduleCandidates(scheduleCandidates)
                                            .build();

                for (ScheduleCandidate each : scheduleCandidates) {
                    each.setSchedule(schedule);
                }

                return schedule;
            }

            ScheduleCandidate scheduleCandidateOf(LocalDateTime startDateTime, LocalDateTime endDateTime) {
                return ScheduleCandidate.builder()
                                        .startDateTime(startDateTime)
                                        .endDateTime(endDateTime)
                                        .build();
            }
        }

        Provider provider = new Provider();

        Supplier<Arguments> equalRange = () -> {
            List<ScheduleCandidate> scheduleCandidates = List.of(
                    provider.scheduleCandidateOf(
                            LocalDateTime.of(2021, 9, 1, 0, 0),
                            LocalDateTime.of(2021, 9, 1, 23, 59)
                    )
            );
            Schedule schedule = provider.scheduleFrom(scheduleCandidates);

            return Arguments.arguments(
                    "범위 일치",
                    List.of(schedule),
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 2),
                    scheduleCandidates
            );
        };

        Supplier<Arguments> insideRange = () -> {
            List<ScheduleCandidate> scheduleCandidates = List.of(
                    provider.scheduleCandidateOf(
                            LocalDateTime.of(2021, 9, 1, 1, 0),
                            LocalDateTime.of(2021, 9, 1, 22, 59)
                    )
            );
            Schedule schedule = provider.scheduleFrom(scheduleCandidates);

            return Arguments.arguments(
                    "범위 안쪽",
                    List.of(schedule),
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 2),
                    scheduleCandidates
            );
        };

        Supplier<Arguments> leftOutAndRightEqual = () -> {
            List<ScheduleCandidate> scheduleCandidates = List.of(
                    provider.scheduleCandidateOf(
                            LocalDateTime.of(2021, 8, 31, 23, 59),
                            LocalDateTime.of(2021, 9, 1, 23, 59)
                    )
            );
            Schedule schedule = provider.scheduleFrom(scheduleCandidates);

            return Arguments.arguments(
                    "범위 안쪽이면서 왼쪽으로 벗어나게",
                    List.of(schedule),
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 2),
                    scheduleCandidates
            );
        };

        Supplier<Arguments> leftEqualAndRightOut = () -> {
            List<ScheduleCandidate> scheduleCandidates = List.of(
                    provider.scheduleCandidateOf(
                            LocalDateTime.of(2021, 9, 1, 0, 0),
                            LocalDateTime.of(2021, 9, 2, 0, 0)
                    )
            );
            Schedule schedule = provider.scheduleFrom(scheduleCandidates);

            return Arguments.arguments(
                    "범위 안쪽이면서 오른쪽으로 벗어나게",
                    List.of(schedule),
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 2),
                    scheduleCandidates
            );
        };

        Supplier<Arguments> leftOutAndRightIn = () -> {
            List<ScheduleCandidate> scheduleCandidates = List.of(
                    provider.scheduleCandidateOf(
                            LocalDateTime.of(2021, 8, 31, 23, 59),
                            LocalDateTime.of(2021, 9, 1, 0, 1)
                    )
            );
            Schedule schedule = provider.scheduleFrom(scheduleCandidates);

            return Arguments.arguments(
                    "범위 왼쪽에 걸치게",
                    List.of(schedule),
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 2),
                    scheduleCandidates
            );
        };

        Supplier<Arguments> leftOut = () -> {
            List<ScheduleCandidate> scheduleCandidates = List.of(
                    provider.scheduleCandidateOf(
                            LocalDateTime.of(2021, 8, 31, 23, 59),
                            LocalDateTime.of(2021, 9, 1, 0, 0)
                    )
            );
            Schedule schedule = provider.scheduleFrom(scheduleCandidates);

            return Arguments.arguments(
                    "범위 왼쪽 벗어남",
                    List.of(schedule),
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 2),
                    Collections.emptyList()
            );
        };

        Supplier<Arguments> leftInAndRightOut = () -> {
            List<ScheduleCandidate> scheduleCandidates = List.of(
                    provider.scheduleCandidateOf(
                            LocalDateTime.of(2021, 9, 1, 23, 59),
                            LocalDateTime.of(2021, 9, 2, 0, 0)
                    )
            );
            Schedule schedule = provider.scheduleFrom(scheduleCandidates);

            return Arguments.arguments(
                    "범위 오른쪽에 걸치게",
                    List.of(schedule),
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 2),
                    scheduleCandidates
            );
        };

        Supplier<Arguments> rightOut = () -> {
            List<ScheduleCandidate> scheduleCandidates = List.of(
                    provider.scheduleCandidateOf(
                            LocalDateTime.of(2021, 9, 2, 0, 0),
                            LocalDateTime.of(2021, 9, 2, 0, 1)
                    )
            );
            Schedule schedule = provider.scheduleFrom(scheduleCandidates);

            return Arguments.arguments(
                    "범위 오른쪽 벗어남",
                    List.of(schedule),
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 2),
                    Collections.emptyList()
            );
        };

        Supplier<Arguments> outerRange = () -> {
            List<ScheduleCandidate> scheduleCandidates = List.of(
                    provider.scheduleCandidateOf(
                            LocalDateTime.of(2021, 8, 31, 23, 59),
                            LocalDateTime.of(2021, 9, 2, 0, 1)
                    )
            );
            Schedule schedule = provider.scheduleFrom(scheduleCandidates);

            return Arguments.arguments(
                    "범위 둘러쌈",
                    List.of(schedule),
                    LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 2),
                    scheduleCandidates
            );
        };

        return Stream.of(
                equalRange.get(),
                insideRange.get(),
                leftOutAndRightEqual.get(),
                leftEqualAndRightOut.get(),
                leftOutAndRightIn.get(),
                leftOut.get(),
                leftInAndRightOut.get(),
                rightOut.get(),
                outerRange.get()
        );
    }
}
