package com.postsquad.scoup.web.schedule.service;

import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleReadAllResponse;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleReadAllResponses;
import com.postsquad.scoup.web.schedule.mapper.ConfirmedScheduleReadAllResponseMapper;
import com.postsquad.scoup.web.schedule.repository.ConfirmedScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ConfirmedScheduleService {

    private final ConfirmedScheduleRepository confirmedScheduleRepository;

    public ConfirmedScheduleReadAllResponses readAll(Long groupId) {
        List<ConfirmedScheduleReadAllResponse> confirmedSchedules = confirmedScheduleRepository.findConfirmedSchedulesBySchedule_Group_Id(groupId)
                                                                                               .stream()
                                                                                               .map(ConfirmedScheduleReadAllResponseMapper.INSTANCE::map)
                                                                                               .collect(Collectors.toList());
        return ConfirmedScheduleReadAllResponses.from(confirmedSchedules);
    }
}
