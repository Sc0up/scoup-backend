package com.postsquad.scoup.web.schedule.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import com.postsquad.scoup.web.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ConfirmedSchedule extends BaseEntity {

    @OneToOne(mappedBy = "confirmedSchedule")
    @Setter
    private Schedule schedule;

    private String colorCode;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @OneToMany
    @JoinColumn
    List<User> confirmedParticipants = new ArrayList<>();

    protected ConfirmedSchedule(Schedule schedule, String colorCode, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.schedule = schedule;
        this.colorCode = colorCode;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Builder
    public static ConfirmedSchedule of(Schedule schedule, String colorCode, LocalDateTime startDateTime, LocalDateTime endDateTime, @Singular List<User> confirmedParticipants) {
        ConfirmedSchedule confirmedSchedule = new ConfirmedSchedule(schedule, colorCode, startDateTime, endDateTime);
        confirmedSchedule.addConfirmedParticipants(confirmedParticipants);
        return confirmedSchedule;
    }

    public void addConfirmedParticipants(List<User> confirmedParticipants) {
        this.confirmedParticipants.addAll(confirmedParticipants);
    }
}
