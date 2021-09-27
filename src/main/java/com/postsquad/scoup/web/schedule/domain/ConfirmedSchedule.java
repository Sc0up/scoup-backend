package com.postsquad.scoup.web.schedule.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import com.postsquad.scoup.web.user.domain.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ConfirmedSchedule extends BaseEntity {

    private static final String DEFAULT_COLOR_CODE = "#00ff0000";

    @OneToOne(mappedBy = "confirmedSchedule")
    @Setter
    private Schedule schedule;

    @ColumnDefault("'" + DEFAULT_COLOR_CODE + "'")
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
