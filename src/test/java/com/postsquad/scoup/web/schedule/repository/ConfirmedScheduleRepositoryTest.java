package com.postsquad.scoup.web.schedule.repository;

import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.schedule.domain.ConfirmedSchedule;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.provider.FindConfirmedSchedulesByGroupIdProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConfirmedScheduleRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ConfirmedScheduleRepository confirmedScheduleRepository;

    @ParameterizedTest
    @ArgumentsSource(FindConfirmedSchedulesByGroupIdProvider.class)
    void findConfirmedSchedulesByGroupId(
            String description,
            List<Group> givenGroups,
            ConfirmedSchedule givenConfirmedSchedule,
            ConfirmedSchedule expectedConfirmedSchedule
    ) {
        // given
        givenGroups.forEach(group -> entityManager.persist(group));

        Group group = entityManager.find(Group.class, 2L);
        Schedule schedule = givenConfirmedSchedule.getSchedule();
        schedule.setGroup(group);
        schedule.setConfirmedSchedule(givenConfirmedSchedule);
        entityManager.persist(schedule);

        ConfirmedSchedule confirmedSchedule = confirmedScheduleRepository.save(givenConfirmedSchedule);
        Long groupId = confirmedSchedule.getSchedule().getGroup().getId();

        // when
        List<ConfirmedSchedule> actualConfirmedSchedules = confirmedScheduleRepository.findConfirmedSchedulesBySchedule_Group_Id(groupId);

        // then
        then(actualConfirmedSchedules.get(0))
                .as(description)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdDateTime", "modifiedDateTime", "schedule")
                .isEqualTo(expectedConfirmedSchedule);
    }
}
