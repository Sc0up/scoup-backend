package com.postsquad.scoup.web.schedule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UnconfirmedScheduleReadAllResponse extends ScheduleBaseResponse {
    private List<ScheduleCandidateResponseForUnconfirmedSchedule> scheduleCandidates;
}
