package com.postsquad.scoup.web.schedule.repository;

import com.postsquad.scoup.web.schedule.domain.ConfirmedSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConfirmedScheduleRepository extends JpaRepository<ConfirmedSchedule, Long> {

    @Query("SELECT cs FROM ConfirmedSchedule cs" +
           " JOIN FETCH cs.schedule s" +
           " WHERE cs.schedule.group.id = :id"
    )
    List<ConfirmedSchedule> findConfirmedSchedulesByGroupId(@Param("id") Long id);
}
