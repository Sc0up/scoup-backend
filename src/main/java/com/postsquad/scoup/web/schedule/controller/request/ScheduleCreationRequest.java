package com.postsquad.scoup.web.schedule.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScheduleCreationRequest {

    @NotEmpty
    private String title;

    private String description;

    private LocalDateTime pollDueDateTime;

    @Builder.Default
    private Boolean isPollAnonymous = false;

    @Size(min = 1, max = 20)
    private List<ScheduleCandidateCreationRequest> scheduleCandidates;
}
