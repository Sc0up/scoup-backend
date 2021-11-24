package com.postsquad.scoup.web.schedule.controller;

import com.postsquad.scoup.web.schedule.controller.response.ConfirmedParticipantResponse;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateResponseForUnconfirmedSchedule;
import com.postsquad.scoup.web.schedule.controller.response.UnconfirmedScheduleReadAllResponse;
import com.postsquad.scoup.web.schedule.controller.response.UnconfirmedScheduleReadAllResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UnconfirmedScheduleController {

    @GetMapping("/groups/{groupId}/unconfirmed-schedules")
    public UnconfirmedScheduleReadAllResponses readConfirmedSchedules(@PathVariable long groupId) {
        return UnconfirmedScheduleReadAllResponses.of(List.of(
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
    }
}
