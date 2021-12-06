package com.postsquad.scoup.web.schedule.controller;

import com.postsquad.scoup.web.common.DefaultPostResponse;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCandidateCreationRequest;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCandidateReadRequest;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponses;
import com.postsquad.scoup.web.schedule.service.ScheduleCandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ScheduleCandidateController {

    private final ScheduleCandidateService scheduleCandidateService;

    @GetMapping("/groups/{groupId}/schedule-candidates")
    public ScheduleCandidateReadAllResponses readAll(@PathVariable long groupId, ScheduleCandidateReadRequest givenScheduleCandidateReadRequest) {
        return scheduleCandidateService.readAll(groupId, givenScheduleCandidateReadRequest);
    }

    @PostMapping("/groups/{groupId}/schedules/{scheduleId}/candidates")
    @ResponseStatus(HttpStatus.CREATED)
    public DefaultPostResponse create(@PathVariable long groupId, @PathVariable long scheduleId, @RequestBody ScheduleCandidateCreationRequest scheduleCandidateCreationRequest) {
        return DefaultPostResponse.builder().id(1L).build();
    }

    @DeleteMapping("/groups/{groupId}/schedules/{scheduleId}/candidates/{candidateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long groupId, @PathVariable long scheduleId, @PathVariable long candidateId) {
    }
}
