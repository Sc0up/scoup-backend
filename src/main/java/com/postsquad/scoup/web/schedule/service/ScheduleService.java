package com.postsquad.scoup.web.schedule.service;

import com.postsquad.scoup.web.common.DefaultPostResponse;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCreationRequest;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.mapper.ScheduleMapper;
import com.postsquad.scoup.web.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public DefaultPostResponse create(long groupId, ScheduleCreationRequest scheduleCreationRequest) {
        Schedule scheduleToSave = ScheduleMapper.INSTANCE.map(groupId, scheduleCreationRequest);
        scheduleRepository.save(scheduleToSave);
        return DefaultPostResponse.from(scheduleToSave);
    }
}
