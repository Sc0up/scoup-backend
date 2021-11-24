package com.postsquad.scoup.web.schedule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ConfirmedSchedulePeriodReadAllResponse {

    private LocalDateTime start_date_time;

    private LocalDateTime end_date_time;
}
