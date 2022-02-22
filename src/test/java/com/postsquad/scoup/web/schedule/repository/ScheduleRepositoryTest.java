package com.postsquad.scoup.web.schedule.repository;

import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.group.repository.GroupRepository;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void save() {
        Group givenGroup = Group.builder()
                           .name("name")
                           .build();

        groupRepository.save(givenGroup);

        entityManager.flush();
        entityManager.clear();

        Group targetGroup = Group.from(givenGroup.getId());

        Schedule schedule = Schedule.builder()
                                    .title("title")
                                    .group(targetGroup)
                                    .build();

        scheduleRepository.save(schedule);
        entityManager.flush();
        entityManager.clear();

        Group actualGroup = groupRepository.findById(targetGroup.getId()).orElseThrow();
        assertThat(actualGroup.getSchedules()).hasSize(1);
    }
}
