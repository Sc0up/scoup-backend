package com.postsquad.scoup.web.schedule.controller.request;

import javax.validation.constraints.NotNull;

public class SchedulePollCancellationRequest {

    @NotNull
    private Long candidateId;
}
