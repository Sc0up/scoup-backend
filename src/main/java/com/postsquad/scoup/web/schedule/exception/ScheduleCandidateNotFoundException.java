package com.postsquad.scoup.web.schedule.exception;

public class ScheduleCandidateNotFoundException extends RuntimeException{

    public ScheduleCandidateNotFoundException() {
        super("Schedule Candidate Not Found");
    }
}
