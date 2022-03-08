package com.postsquad.scoup.web.schedule.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import com.postsquad.scoup.web.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

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

    @OneToMany
    private Set<User> polledUser;

    public ScheduleCandidate(Schedule schedule, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.schedule = schedule;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Builder
    public static ScheduleCandidate of(Schedule schedule, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return new ScheduleCandidate(schedule, startDateTime, endDateTime);
    }

    public void poll(User user) {
        polledUser.add(user);
    }

    public int pollCount() {
        return polledUser.size();
    }
}
