package com.postsquad.scoup.web.group.controller.response;

import com.postsquad.scoup.web.schedule.controller.response.ScheduleResponses;
import lombok.Getter;

@Getter
public class GroupDetailResponse extends GroupBaseResponse {

    private GroupMemberResponses members;

    private ScheduleResponses schedules;
}
