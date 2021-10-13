package com.postsquad.scoup.web.schedule.controller;

import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleReadAllResponses;
import com.postsquad.scoup.web.schedule.service.ConfirmedScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ConfirmedScheduleController {

    private final ConfirmedScheduleService confirmedScheduleService;

    @GetMapping("/groups/{groupId}/confirmed-schedules")
    public ConfirmedScheduleReadAllResponses readConfirmedSchedules(@PathVariable Long groupId) {
        return confirmedScheduleService.readAll(groupId);
    }
}
