package com.postsquad.scoup.web.schedule;

import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Component
@Transactional
public class ScheduleCandidateDatabaseHelper {
    @Autowired
    EntityManager entityManager;

    public void setSchedules(List<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            entityManager.persist(schedule);

            for(ScheduleCandidate scheduleCandidate : schedule.getScheduleCandidates()) {
                scheduleCandidate.setSchedule(schedule);
                entityManager.persist(scheduleCandidate);
            }
        }
    }
}
