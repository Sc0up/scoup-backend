package com.postsquad.scoup.web.schedule.controller.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class ScheduleCandidateRegistrationRequest {

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;
    //TODO: startDateTime이 endDateTime 이전이어야 함.
}
