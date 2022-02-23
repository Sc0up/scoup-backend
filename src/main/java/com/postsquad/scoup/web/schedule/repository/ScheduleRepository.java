package com.postsquad.scoup.web.schedule.repository;

import com.postsquad.scoup.web.schedule.domain.Schedule;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
}
