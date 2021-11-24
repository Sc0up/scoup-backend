package com.postsquad.scoup.web.schedule.controller.request;

import lombok.*;

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
    //TODO: startDateTime이 endDateTime 이전이어야 함.
}
