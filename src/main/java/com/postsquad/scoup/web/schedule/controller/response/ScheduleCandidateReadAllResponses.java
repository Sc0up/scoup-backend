package com.postsquad.scoup.web.schedule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor(staticName = "from")
@Data
public class ScheduleCandidateReadAllResponses {

    private Map<LocalDate, List<ScheduleCandidateReadAllResponse>> scheduleCandidateResponses;
}
