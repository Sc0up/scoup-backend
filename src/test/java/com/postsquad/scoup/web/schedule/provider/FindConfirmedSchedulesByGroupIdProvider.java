package com.postsquad.scoup.web.schedule.provider;

import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.schedule.domain.ConfirmedSchedule;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

public class FindConfirmedSchedulesByGroupIdProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "성공 - 그룹 id에 따른 확정 일정 조회",
                        Arrays.asList(Group.builder()
                                           .name("group1")
                                           .description("description")
                                           .build(),
                                      Group.builder()
                                           .name("group2")
                                           .description("description")
                                           .build(),
                                      Group.builder()
                                           .name("group3")
                                           .description("description")
                                           .build()),
                        ConfirmedSchedule.builder()
                                         .schedule(Schedule.builder()
                                                           .title("schedule title")
                                                           .description("schedule description")
                                                           .build())
                                         .startDateTime(LocalDateTime.of(2021, 9, 25, 9, 0))
                                         .endDateTime(LocalDateTime.of(2021, 9, 25, 11, 0))
                                         .build(),
                        ConfirmedSchedule.builder()
                                         .schedule(Schedule.builder()
                                                           .title("schedule title")
                                                           .group(Group.builder()
                                                                       .name("group2")
                                                                       .description("description")
                                                                       .build())
                                                           .description("schedule description")
                                                           .build())
                                         .startDateTime(LocalDateTime.of(2021, 9, 25, 9, 0))
                                         .endDateTime(LocalDateTime.of(2021, 9, 25, 11, 0))
                                         .build()
                )
        );
    }
}
