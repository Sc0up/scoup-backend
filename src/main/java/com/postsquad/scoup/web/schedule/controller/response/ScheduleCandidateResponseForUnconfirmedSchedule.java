package com.postsquad.scoup.web.schedule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ScheduleCandidateResponseForUnconfirmedSchedule {

    private Long candidateId;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime dueDateTime;

    private List<ConfirmedParticipantResponse> confirmedParticipants;
}
