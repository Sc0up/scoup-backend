package com.postsquad.scoup.web.schedule.controller;

import com.postsquad.scoup.web.schedule.controller.request.SchedulePollRequest;
import com.postsquad.scoup.web.schedule.controller.response.SchedulePollResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SchedulePollController {

    @PostMapping("/groups/{groupId}/schedules/{scheduleId}/poll")
    @ResponseStatus(HttpStatus.CREATED)
    public SchedulePollResponse pollSchedule(@PathVariable long groupId, @PathVariable long scheduleId, SchedulePollRequest schedulePollRequest) {
        return SchedulePollResponse.builder()
                                   .pollCount(1)
                                   .build();
    }

    @DeleteMapping("/groups/{groupId}/schedules/{scheduleId}/poll")
    @ResponseStatus(HttpStatus.OK)
    public SchedulePollResponse cancelPollSchedule(@PathVariable long groupId, @PathVariable long scheduleId, SchedulePollRequest schedulePollRequest) {
        return SchedulePollResponse.builder()
                                   .pollCount(1)
                                   .build();
    }
}
