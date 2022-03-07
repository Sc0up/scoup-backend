package com.postsquad.scoup.web.schedule.service;

import com.postsquad.scoup.web.schedule.controller.request.SchedulePollRequest;
import com.postsquad.scoup.web.schedule.controller.response.SchedulePollResponse;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import com.postsquad.scoup.web.schedule.exception.ScheduleCandidateNotFoundException;
import com.postsquad.scoup.web.schedule.repository.ScheduleCandidateRepository;
import com.postsquad.scoup.web.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SchedulePollService {

    private final ScheduleCandidateRepository scheduleCandidateRepository;

    @Transactional
    public SchedulePollResponse poll(SchedulePollRequest schedulePollRequest, User user) {
        ScheduleCandidate scheduleCandidate = scheduleCandidateRepository
                .findById(schedulePollRequest.getScheduleCandidateId())
                .orElseThrow(ScheduleCandidateNotFoundException::new);
        scheduleCandidate.poll(user);

        return SchedulePollResponse.builder()
                .pollCount(scheduleCandidate.pollCount())
                .build();
    }
}
