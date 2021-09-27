package com.postsquad.scoup.web.schedule.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ScheduleCandidate extends BaseEntity {

    private static final String DEFAULT_COLOR_CODE = "#00ff0000";

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @Setter
    private Schedule schedule;

    @ColumnDefault("'" + DEFAULT_COLOR_CODE + "'")
    private String colorCode;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    public ScheduleCandidate(Schedule schedule, String colorCode, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.schedule = schedule;
        this.colorCode = colorCode;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Builder
    public static ScheduleCandidate of(Schedule schedule, String colorCode, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return new ScheduleCandidate(schedule, colorCode, startDateTime, endDateTime);
    }
}
