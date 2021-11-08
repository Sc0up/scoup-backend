package com.postsquad.scoup.web.schedule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ScheduleCandidateReadAllResponse {

    private long id;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Builder.Default
    private Boolean isConfirmed = false;

    private long scheduleId;

    private String scheduleTitle;

    private String scheduleDescription;

    private String colorCode;
}
