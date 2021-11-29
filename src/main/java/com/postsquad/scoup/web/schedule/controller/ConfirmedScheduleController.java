package com.postsquad.scoup.web.schedule.controller;

import com.postsquad.scoup.web.schedule.controller.request.ConfirmedSchedulePeriodReadAllRequest;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedSchedulePeriodReadAllResponse;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleReadAllResponses;
import com.postsquad.scoup.web.schedule.service.ConfirmedScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ConfirmedScheduleController {

    private final ConfirmedScheduleService confirmedScheduleService;

    @GetMapping("/groups/{groupId}/confirmed-schedules")
    public ConfirmedScheduleReadAllResponses readConfirmedSchedules(@PathVariable Long groupId) {
        return confirmedScheduleService.readAll(groupId);
    }

    @GetMapping("/groups/{groupId}/existing-schedules")
    public List<ConfirmedSchedulePeriodReadAllResponse> readConfirmedSchedulePeriods(@PathVariable Long groupId, ConfirmedSchedulePeriodReadAllRequest confirmedSchedulePeriodReadAllRequest) {
        return List.of(
                ConfirmedSchedulePeriodReadAllResponse.builder()
                                                      .startDateTime(LocalDateTime.of(2021, 11, 25, 0, 0))
                                                      .endDateTime(LocalDateTime.of(2021, 11, 26, 0, 0))
                                                      .build()
        );
    }
}
