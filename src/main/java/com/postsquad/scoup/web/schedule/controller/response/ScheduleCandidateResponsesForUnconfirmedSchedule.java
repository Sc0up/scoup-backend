package com.postsquad.scoup.web.schedule.controller.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ScheduleCandidateResponsesForUnconfirmedSchedule {

    private List<ScheduleCandidateResponseForUnconfirmedSchedule> scheduleCandidates;
}
