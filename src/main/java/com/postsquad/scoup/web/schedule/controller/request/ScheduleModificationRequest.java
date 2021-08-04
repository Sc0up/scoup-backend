package com.postsquad.scoup.web.schedule.controller.request;

import javax.validation.constraints.NotEmpty;

public class ScheduleModificationRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;
}
