package com.postsquad.scoup.web.schedule.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ConfirmedSchedulePeriodReadAllRequest {

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
