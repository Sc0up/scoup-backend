package com.postsquad.scoup.web.schedule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScheduleReadOneResponse {

    private long id;

    private String title;

    private String description;

    private LocalDateTime pollDueDateTime;

    private ConfirmedScheduleResponseForReadOneSchedule confirmedSchedule;

    private List<ScheduleCandidateResponseForReadOneSchedule> scheduleCandidates;
}
