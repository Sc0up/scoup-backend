package com.postsquad.scoup.web.schedule.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ScheduleCandidate extends BaseEntity {

    private boolean isConfirmed;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @ManyToOne
    private Schedule schedule;

    public ScheduleCandidate(boolean isConfirmed, LocalDateTime startDateTime, LocalDateTime endDateTime, Schedule schedule) {
        this.isConfirmed = isConfirmed;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.schedule = schedule;
    }

    @Builder
    public static ScheduleCandidate of(boolean isConfirmed, LocalDateTime startDateTime, LocalDateTime endDateTime, Schedule schedule) {
        return new ScheduleCandidate(isConfirmed, startDateTime, endDateTime, schedule);
    }
}
