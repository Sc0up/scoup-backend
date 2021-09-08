package com.postsquad.scoup.web.schedule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class ConfirmedScheduleResponse extends ScheduleBaseResponse {

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private List<ConfirmedParticipantResponse> confirmedParticipants;
}
