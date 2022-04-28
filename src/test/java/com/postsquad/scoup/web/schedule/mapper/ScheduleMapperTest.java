package com.postsquad.scoup.web.schedule.mapper;

import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCreationRequest;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedParticipantResponse;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleResponseForReadOneSchedule;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateResponseForReadOneSchedule;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleReadOneResponse;
import com.postsquad.scoup.web.schedule.domain.ConfirmedSchedule;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleMapperTest {

    @Test
    void mapFromScheduleCreationRequest() {
        long givenGroupId = 1L;
        ScheduleCreationRequest givenScheduleCreationRequest = ScheduleCreationRequest.builder()
                                                                                      .title("title")
                                                                                      .scheduleCandidates(List.of())
                                                                                      .build();

        Schedule actualSchedule = ScheduleMapper.INSTANCE.map(givenGroupId, givenScheduleCreationRequest);

        long expectedGroupId = givenGroupId;
        String expectedScheduleTitle = givenScheduleCreationRequest.getTitle();

        assertThat(actualSchedule)
                .hasFieldOrPropertyWithValue("group.id", expectedGroupId)
                .hasFieldOrPropertyWithValue("title", expectedScheduleTitle);

    }

    @Test
    void toScheduleReadOneResponse() {
        User testUser = User.builder()
                            .nickname("nickname")
                            .email("email@email.com")
                            .password("password")
                            .avatarUrl("url")
                            .username("username")
                            .oAuthUsers(List.of(OAuthUser.of(OAuthType.NONE, "")))
                            .build();
        ConfirmedSchedule confirmedSchedule = ConfirmedSchedule.builder()
                                                               .startDateTime(LocalDateTime.of(2021, 11, 22, 0, 0))
                                                               .endDateTime(LocalDateTime.of(2021, 11, 23, 0, 0))
                                                               .confirmedParticipant(testUser)
                                                               .build();
        ScheduleCandidate scheduleCandidate = ScheduleCandidate.builder()
                                                               .startDateTime(LocalDateTime.of(2021, 11, 22, 0, 0))
                                                               .endDateTime(LocalDateTime.of(2021, 11, 23, 0, 0))
                                                               .build();
        scheduleCandidate.poll(testUser);

        Schedule schedule = Schedule.builder()
                                    .title("schedule title")
                                    .description("schedule description")
                                    .dueDateTime(LocalDateTime.of(2021, 11, 24, 0, 0))
                                    .confirmedSchedule(confirmedSchedule)
                                    .scheduleCandidate(scheduleCandidate)
                                    .build();

        ScheduleReadOneResponse expectedScheduleReadOneResponse =
                ScheduleReadOneResponse
                        .builder()
//                        .id(schedule.getId())
                        .title("schedule title")
                        .description("schedule description")
                        .pollDueDateTime(LocalDateTime.of(2021, 11, 24, 0, 0))
                        .confirmedSchedule(
                                ConfirmedScheduleResponseForReadOneSchedule
                                        .builder()
//                                        .id(confirmedSchedule.getId())
                                        .startDateTime(LocalDateTime.of(2021, 11, 22, 0, 0))
                                        .endDateTime(LocalDateTime.of(2021, 11, 23, 0, 0))
                                        .confirmedParticipants(List.of(
                                                ConfirmedParticipantResponse
                                                        .builder()
                                                        .nickname("nickname")
                                                        .username("username")
                                                        .build()
                                        ))
                                        .build()
                        )
                        .scheduleCandidates(List.of(
                                ScheduleCandidateResponseForReadOneSchedule
                                        .builder()
//                                        .id(scheduleCandidate.getId())
                                        .startDateTime(LocalDateTime.of(2021, 11, 22, 0, 0))
                                        .endDateTime(LocalDateTime.of(2021, 11, 23, 0, 0))
                                        .pollCount(1)
                                        .confirmedParticipants(List.of(
                                                ConfirmedParticipantResponse
                                                        .builder()
                                                        .nickname("nickname")
                                                        .username("username")
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .build();

        ScheduleReadOneResponse actual = ScheduleMapper.INSTANCE.toScheduleReadOneResponse(schedule);

        assertThat(actual).usingRecursiveComparison()
                          .isEqualTo(expectedScheduleReadOneResponse);

    }
}
