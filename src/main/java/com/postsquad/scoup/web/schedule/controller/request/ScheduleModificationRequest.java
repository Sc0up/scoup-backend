package com.postsquad.scoup.web.schedule.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScheduleModificationRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;
}
