package com.postsquad.scoup.web.schedule.controller.request;

import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateResponses;

import java.time.LocalDateTime;

public class ScheduleRegistrationRequest {

    private String title;

    private String description;

    private boolean isConfirmed;

    private int 일정확정최소인원;

    private LocalDateTime pollDueDate;

    private ScheduleCandidateResponses scheduleCandidates;

    private boolean 일정확정후멤버추가가능여부;
}
