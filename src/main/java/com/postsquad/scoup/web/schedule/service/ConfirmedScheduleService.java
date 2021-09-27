package com.postsquad.scoup.web.schedule.service;

import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleReadAllResponses;
import com.postsquad.scoup.web.schedule.mapper.ConfirmedScheduleReadAllResponseMapper;
import com.postsquad.scoup.web.schedule.repository.ConfirmedScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ConfirmedScheduleService {

    private final ConfirmedScheduleRepository confirmedScheduleRepository;

    public ConfirmedScheduleReadAllResponses readAll(Long groupId) {
        return ConfirmedScheduleReadAllResponses.from(confirmedScheduleRepository.findConfirmedSchedulesBySchedule_Group_Id(groupId)
                                                                                 .stream()
                                                                                 .map(ConfirmedScheduleReadAllResponseMapper.INSTANCE::map)
                                                                                 .collect(Collectors.toList()));
    }
}
