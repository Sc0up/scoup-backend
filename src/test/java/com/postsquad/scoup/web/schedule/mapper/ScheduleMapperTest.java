package com.postsquad.scoup.web.schedule.mapper;

import com.postsquad.scoup.web.schedule.controller.request.ScheduleCreationRequest;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleMapperTest {

    @Test
    void mapFromScheduleCreationRequest() {
        long givenGroupId = 1L;
        ScheduleCreationRequest givenScheduleCreationRequest = ScheduleCreationRequest.builder()
                                                                                      .title("title")
                                                                                      .scheduleCandidates(List.of())
                                                                                      .build();

        Schedule actualSchedule = ScheduleMapper.INSTANCE.map(givenGroupId, givenScheduleCreationRequest);

        long expectedGroupId = givenGroupId;
        String expectedScheduleTitle = givenScheduleCreationRequest.getTitle();

        assertThat(actualSchedule)
                .hasFieldOrPropertyWithValue("group.id", expectedGroupId)
                .hasFieldOrPropertyWithValue("title", expectedScheduleTitle);

    }
}
