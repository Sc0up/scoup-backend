package com.postsquad.scoup.web.schedule.mapper;

import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCreationRequest;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedParticipantResponse;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateResponseForReadOneSchedule;
import com.postsquad.scoup.web.schedule.controller.response.ScheduleReadOneResponse;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import com.postsquad.scoup.web.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface ScheduleMapper {

    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    @Mapping(target = "group", source = "groupId", qualifiedByName = "group")
    Schedule map(Long groupId, ScheduleCreationRequest scheduleCreationRequest);

    @Named("group")
    default Group group(long groupId) {
        return Group.from(groupId);
    }

    @Mapping(target = "pollDueDateTime", source = "dueDateTime")
    ScheduleReadOneResponse toScheduleReadOneResponse(Schedule schedule);

    @Mapping(target = "confirmedParticipants", source = "polledUser")
    @Mapping(target = "pollCount", source = "scheduleCandidate",  qualifiedByName = "pollCount")
    ScheduleCandidateResponseForReadOneSchedule scheduleCandidateToScheduleCandidateResponseForReadOneSchedule(ScheduleCandidate scheduleCandidate);

    @Named("pollCount")
    default int pollCount(ScheduleCandidate scheduleCandidate) {
        return scheduleCandidate.pollCount();
    }
}
