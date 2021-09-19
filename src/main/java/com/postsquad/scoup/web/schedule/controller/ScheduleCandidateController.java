package com.postsquad.scoup.web.schedule.controller;

import com.postsquad.scoup.web.schedule.controller.request.ScheduleCandidateReadRequest;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponses;
import com.postsquad.scoup.web.schedule.service.ScheduleCandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ScheduleCandidateController {

    private final ScheduleCandidateService scheduleCandidateService;

    @GetMapping("/groups/{groupId}/schedule-candidates")
    public ScheduleCandidateReadAllResponses readAll(@PathVariable long groupId, ScheduleCandidateReadRequest givenScheduleCandidateReadRequest) {
        return scheduleCandidateService.readAll(groupId, givenScheduleCandidateReadRequest);
    }
}
