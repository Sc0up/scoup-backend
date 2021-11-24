package com.postsquad.scoup.web.schedule.controller;

import com.postsquad.scoup.web.common.DefaultPostResponse;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleConfirmationRequest;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCreationRequest;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleModificationRequest;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedParticipantResponse;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleResponseForReadOneSchedule;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateResponseForReadOneSchedule;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleReadOneResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ScheduleController {

    @GetMapping("/groups/{groupId}/schedules/{scheduleId}")
    public ScheduleReadOneResponse readOne(@PathVariable long groupId, @PathVariable long scheduleId) {
        return ScheduleReadOneResponse.builder()
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
    }

    @PostMapping("/groups/{groupId}/schedules")
    @ResponseStatus(HttpStatus.CREATED)
    public DefaultPostResponse create(@PathVariable long groupId, ScheduleCreationRequest scheduleCreationRequest) {
        return DefaultPostResponse.builder()
                                  .id(1L)
                                  .build();
    }

    @PatchMapping("/groups/{groupId}/schedules/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long groupId, @PathVariable long scheduleId, @RequestBody ScheduleModificationRequest scheduleModificationRequest) {
    }

    @PatchMapping("/groups/{groupId}/schedules/{scheduleId}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmSchedule(@PathVariable long groupId, @PathVariable long scheduleId, @RequestBody ScheduleConfirmationRequest scheduleConfirmationRequest) {
    }
}
