package com.postsquad.scoup.web.group.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import com.postsquad.scoup.web.group.controller.request.GroupModificationRequest;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.*;

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

    @ManyToMany
    @JoinTable(
            name = "group_member",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private final List<User> members = new ArrayList<>();

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
        this.schedules.add(schedule);
        if (schedule.getGroup() != this) {
            schedule.setGroup(this);
        }
    }

    public void addSchedules(List<Schedule> schedules) {
        this.schedules.addAll(schedules);
    }

    public void addMember(User user) {
        user.getJoinedGroups().add(this);
        this.members.add(user);
    }
}
