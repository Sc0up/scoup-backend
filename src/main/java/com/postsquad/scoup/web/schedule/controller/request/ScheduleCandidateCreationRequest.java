package com.postsquad.scoup.web.schedule.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScheduleCandidateCreationRequest {
    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    @JsonIgnore
    @AssertTrue
    public boolean isDatesValid() {
        return startDateTime.isBefore(endDateTime);
    }
}
