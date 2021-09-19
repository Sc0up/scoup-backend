package com.postsquad.scoup.web.schedule.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Schedule extends BaseEntity {

    private static final String DEFAULT_COLOR_CODE = "#00ff0000";

    private String title;

    private String description;

    private LocalDateTime dueDateTime;

    @ColumnDefault("'" + DEFAULT_COLOR_CODE + "'")
    private String colorCode;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<ScheduleCandidate> scheduleCandidates = new ArrayList<>();

    protected Schedule(String title, String description, LocalDateTime dueDateTime, String colorCode) {
        this.title = title;
        this.description = description;
        this.dueDateTime = dueDateTime;
        this.colorCode = colorCode;
    }

    @Builder
    public static Schedule of(String title, String description, LocalDateTime dueDateTime, String colorCode, List<ScheduleCandidate> scheduleCandidates) {
        Schedule schedule = new Schedule(title, description, dueDateTime, colorCode);
        schedule.addScheduleCandidates(scheduleCandidates);
        return schedule;
    }

    public void addScheduleCandidates(List<ScheduleCandidate> scheduleCandidates) {
        this.scheduleCandidates.addAll(scheduleCandidates);
    }
}
