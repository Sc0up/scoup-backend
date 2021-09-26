package com.postsquad.scoup.web.schedule.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(staticName = "from")
@Builder
@Data
public class ConfirmedScheduleReadAllResponses {

     private List<ConfirmedScheduleReadAllResponse> confirmedSchedules;
}
