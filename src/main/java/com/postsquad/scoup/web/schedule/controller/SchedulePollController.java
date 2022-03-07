package com.postsquad.scoup.web.schedule.controller;

import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.schedule.controller.request.SchedulePollRequest;
import com.postsquad.scoup.web.schedule.controller.response.SchedulePollResponse;
import com.postsquad.scoup.web.schedule.exception.ScheduleCandidateNotFoundException;
import com.postsquad.scoup.web.schedule.service.SchedulePollService;
import com.postsquad.scoup.web.user.LoggedInUser;
import com.postsquad.scoup.web.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SchedulePollController {

    private final SchedulePollService schedulePollService;

    @PostMapping("/groups/{groupId}/schedules/{scheduleId}/poll")
    @ResponseStatus(HttpStatus.CREATED)
    public SchedulePollResponse pollSchedule(@PathVariable long groupId, @PathVariable long scheduleId, @RequestBody SchedulePollRequest schedulePollRequest, @LoggedInUser User user) {
        return schedulePollService.poll(schedulePollRequest, user);
    }

    @ExceptionHandler(ScheduleCandidateNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse scheduleCandidateNotFoundExceptionHandler(ScheduleCandidateNotFoundException scheduleCandidateNotFoundException) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, scheduleCandidateNotFoundException.getMessage());
    }


    @DeleteMapping("/groups/{groupId}/schedules/{scheduleId}/poll")
    @ResponseStatus(HttpStatus.OK)
    public SchedulePollResponse cancelPollSchedule(@PathVariable long groupId, @PathVariable long scheduleId, SchedulePollRequest schedulePollRequest) {
        return SchedulePollResponse.builder()
                                   .pollCount(1)
                                   .build();
    }
}
