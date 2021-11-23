package com.postsquad.scoup.web.schedule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Data
public class UnconfirmedScheduleReadAllResponses {

    private List<UnconfirmedScheduleReadAllResponse> unconfirmedSchedules;
}
