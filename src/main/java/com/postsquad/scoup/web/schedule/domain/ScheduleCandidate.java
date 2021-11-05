package com.postsquad.scoup.web.schedule.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ScheduleCandidate extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @Setter
    private Schedule schedule;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    public ScheduleCandidate(Schedule schedule, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.schedule = schedule;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Builder
    public static ScheduleCandidate of(Schedule schedule, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return new ScheduleCandidate(schedule, startDateTime, endDateTime);
    }
}
