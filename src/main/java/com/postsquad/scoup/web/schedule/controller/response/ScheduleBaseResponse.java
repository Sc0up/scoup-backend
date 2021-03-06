package com.postsquad.scoup.web.schedule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class ScheduleBaseResponse {

    private String title;

    private String description;
}
