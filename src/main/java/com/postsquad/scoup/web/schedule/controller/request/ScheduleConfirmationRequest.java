package com.postsquad.scoup.web.schedule.controller.request;

import javax.validation.constraints.NotNull;

public class ScheduleConfirmationRequest {

    @NotNull
    private Long candidateId;
}