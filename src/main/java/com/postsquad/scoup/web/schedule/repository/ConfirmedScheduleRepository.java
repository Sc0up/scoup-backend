package com.postsquad.scoup.web.schedule.repository;

import com.postsquad.scoup.web.schedule.domain.ConfirmedSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ConfirmedScheduleRepository extends JpaRepository<ConfirmedSchedule, Long> {

    List<ConfirmedSchedule> findConfirmedSchedulesBySchedule_Group_Id(Long id);
}
