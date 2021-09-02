package com.postsquad.scoup.web.schedule.controller;

import com.postsquad.scoup.web.schedule.controller.request.ScheduleCandidateReadRequest;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponse;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@RestController
public class ScheduleCandidateController {

    @GetMapping("/groups/{groupId}/schedule-candidates")
    public ScheduleCandidateReadAllResponses readAll(@PathVariable long groupId, ScheduleCandidateReadRequest givenScheduleCandidateReadRequest) {
        return ScheduleCandidateReadAllResponses.from(
                Map.of(
                        LocalDate.now(),
                        Arrays.asList(
                                ScheduleCandidateReadAllResponse.builder()
                                                                .startDateTime(LocalDateTime.parse("2021-09-01T12:00"))
                                                                .endDateTime(LocalDateTime.parse("2021-09-01T15:00"))
                                                                .isConfirmed(false)
                                                                .scheduleTitle("title")
                                                                .build()
                        ))
        );
    }
}
