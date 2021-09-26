package com.postsquad.scoup.web;

import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.schedule.domain.ConfirmedSchedule;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TestDataSetup {

    @PersistenceContext
    private EntityManager entityManager;

    public void execute() {
        User user = User.builder()
                        .nickname("nickname")
                        .email("email@email.com")
                        .password("password")
                        .avatarUrl("url")
                        .username("username")
                        .oAuthUsers(List.of(OAuthUser.of(OAuthType.NONE, "")))
                        .build();

        Group group = Group.builder()
                           .name("name")
                           .description("")
                           .schedules(new ArrayList<>())
                           .owner(user)
                           .build();

        Schedule schedule = Schedule.builder()
                                    .group(group)
                                    .title("schedule title")
                                    .description("schedule description")
                                    .dueDateTime(LocalDateTime.of(2021, 9, 23, 0, 0))
                                    .build();
        group.addSchedule(schedule);

        ConfirmedSchedule confirmedSchedule = ConfirmedSchedule.builder()
                                                               .colorCode("#CAB8FF")
                                                               .startDateTime(LocalDateTime.of(2021, 9, 25, 9, 0))
                                                               .endDateTime(LocalDateTime.of(2021, 9, 25, 11, 0))
                                                               .confirmedParticipants(List.of(user))
                                                               .build();
        schedule.setConfirmedSchedule(confirmedSchedule);
        entityManager.persist(group);
    }
}
