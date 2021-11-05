package com.postsquad.scoup.web.schedule.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import com.postsquad.scoup.web.group.domain.Group;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Schedule extends BaseEntity {

    private static final String DEFAULT_COLOR_CODE = "#00ff0000";

    @ManyToOne
    @JoinColumn(name = "group_id")
    @Setter
    private Group group;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 200)
    private String description;

    private LocalDateTime dueDateTime;

    @ColumnDefault("'" + DEFAULT_COLOR_CODE + "'")
    private String colorCode;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn
    private ConfirmedSchedule confirmedSchedule;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private final List<ScheduleCandidate> scheduleCandidates = new ArrayList<>();

    protected Schedule(Group group, String title, String description, LocalDateTime dueDateTime, String colorCode, ConfirmedSchedule confirmedSchedule) {
        this.group = group;
        this.title = title;
        this.description = description;
        this.dueDateTime = dueDateTime;
        this.colorCode = colorCode;
        this.confirmedSchedule = confirmedSchedule;
    }

    @Builder
    public static Schedule of(Group group, String title, String description, LocalDateTime dueDateTime, String colorCode, ConfirmedSchedule confirmedSchedule, @Singular List<ScheduleCandidate> scheduleCandidates) {

        if (Objects.isNull(colorCode)) {
            colorCode = DEFAULT_COLOR_CODE;
        }

        Schedule schedule = new Schedule(group, title, description, dueDateTime, colorCode, confirmedSchedule);
        schedule.addScheduleCandidates(scheduleCandidates);
        return schedule;
    }

    public void addScheduleCandidate(ScheduleCandidate scheduleCandidate) {
        this.scheduleCandidates.add(scheduleCandidate);
        if (scheduleCandidate.getSchedule() != this) {
            scheduleCandidate.setSchedule(this);
        }
    }

    public void addScheduleCandidates(List<ScheduleCandidate> scheduleCandidates) {
        this.scheduleCandidates.addAll(scheduleCandidates);
    }

    public void confirmSchedule(ConfirmedSchedule confirmedSchedule) {
        this.confirmedSchedule = confirmedSchedule;
        confirmedSchedule.setSchedule(this);
    }

    public boolean isConfirmed() {
        return this.confirmedSchedule != null;
    }
}
