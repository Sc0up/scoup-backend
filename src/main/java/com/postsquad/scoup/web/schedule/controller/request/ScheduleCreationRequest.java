package com.postsquad.scoup.web.schedule.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScheduleCreationRequest {

    private String title;

    private String description;

    private LocalDateTime pollDueDateTime;

    @Builder.Default
    private Boolean canPollMultiple = false;

    @Builder.Default
    private Boolean isPollAnonymous = false;

    @Builder.Default
    private Boolean isConfirmedImmediately = false;

    private List<ScheduleCandidateCreationRequest> scheduleCandidates;
}
