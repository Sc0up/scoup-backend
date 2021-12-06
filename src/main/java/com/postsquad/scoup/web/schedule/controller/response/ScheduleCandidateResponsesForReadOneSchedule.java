package com.postsquad.scoup.web.schedule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ScheduleCandidateResponsesForReadOneSchedule {

    private List<ScheduleCandidateResponseForReadOneSchedule> scheduleCandidates;
}
