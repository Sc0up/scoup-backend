package com.postsquad.scoup.web.schedule.service;

import com.postsquad.scoup.web.schedule.controller.request.ScheduleCandidateReadRequest;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponse;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponses;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import com.postsquad.scoup.web.schedule.mapper.ScheduleCandidateReadAllResponseMapper;
import com.postsquad.scoup.web.schedule.repository.ScheduleCandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleCandidateService {

    private final ScheduleCandidateRepository scheduleCandidateRepository;

    public ScheduleCandidateReadAllResponses readAll(long groupId, ScheduleCandidateReadRequest givenScheduleCandidateReadRequest) {
        List<ScheduleCandidate> scheduleCandidates = scheduleCandidateRepository.findAllByDateTimeIncluding(
                givenScheduleCandidateReadRequest.getStartDate().atStartOfDay(),
                givenScheduleCandidateReadRequest.getEndDate().atStartOfDay()
        );

        return ScheduleCandidateReadAllResponses.from(
                scheduleCandidates.stream()
                                  .map(ScheduleCandidateReadAllResponseMapper.INSTANCE::map)
                                  .collect(Collectors.<ScheduleCandidateReadAllResponse, LocalDate>groupingBy(
                                          scheduleCandidateResponse -> scheduleCandidateResponse.getStartDateTime().toLocalDate()
                                  ))
        );
    }
}
