package com.postsquad.scoup.web.schedule.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import lombok.*;

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
    @Setter // TODO: 스케줄 생성 시 한 번에 처리하면 제거 가능
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
