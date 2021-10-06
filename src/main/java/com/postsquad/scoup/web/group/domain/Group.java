package com.postsquad.scoup.web.group.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import com.postsquad.scoup.web.group.controller.request.GroupModificationRequest;
import com.postsquad.scoup.web.schedule.domain.ConfirmedSchedule;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import com.postsquad.scoup.web.user.domain.User;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "UK_GROUP_NAME"),
})
@Entity
public class Group extends BaseEntity {

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 200)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private final List<Schedule> schedules = new ArrayList<>();

    protected Group(String name, String description, User owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    @Builder
    public static Group of(String name, String description, User owner, @Singular List<Schedule> schedules) {
        Group group = new Group(name, description, owner);
        group.addSchedules(schedules);
        return group;
    }

    public Group update(GroupModificationRequest groupModificationRequest) {
        this.name = groupModificationRequest.getName();
        this.description = groupModificationRequest.getDescription();
        return this;
    }

    public boolean verifyOwner(User user) {
        return this.owner.equals(user);
    }

    public void addSchedule(Schedule schedule) {
        System.out.println(schedule.getTitle());
        this.schedules.add(schedule);
        if (schedule.getGroup() != this) {
            schedule.setGroup(this);
        }
    }

    public void addSchedules(List<Schedule> schedules) {
        this.schedules.addAll(schedules);
    }
}
