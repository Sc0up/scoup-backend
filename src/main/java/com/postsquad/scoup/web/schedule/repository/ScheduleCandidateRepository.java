package com.postsquad.scoup.web.schedule.repository;

import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleCandidateRepository extends CrudRepository<ScheduleCandidate, Long> {

    // TODO: Group에 종속적이게 되면 이동해야 할 수 있음
    @Query("SELECT sc " +
           "FROM ScheduleCandidate sc JOIN FETCH sc.schedule " +
           "WHERE " +
           // 범위 왼쪽
           "sc.startDateTime <= :startDateTime AND :startDateTime < sc.endDateTime OR " +
           // 범위 오른쪽
           "sc.startDateTime < :endDateTime AND :endDateTime <= sc.endDateTime OR " +
           // 범위 안쪽
           ":startDateTime <= sc.startDateTime  AND sc.endDateTime < :endDateTime")
    List<ScheduleCandidate> findAllByDateTimeIncluding(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);
}
