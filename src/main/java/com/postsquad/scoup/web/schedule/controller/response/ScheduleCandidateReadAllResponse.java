package com.postsquad.scoup.web.schedule.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private boolean isConfirmed;

    private long scheduleId;

    private String scheduleTitle;

    private String scheduleDescription;

    private String colorCode;

    @JsonProperty("is_confirmed")
    public boolean isConfirmed() {
        return isConfirmed;
    }
}
