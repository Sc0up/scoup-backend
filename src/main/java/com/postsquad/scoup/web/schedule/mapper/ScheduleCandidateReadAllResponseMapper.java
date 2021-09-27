package com.postsquad.scoup.web.schedule.mapper;

import com.postsquad.scoup.web.schedule.controller.response.ScheduleCandidateReadAllResponse;
import com.postsquad.scoup.web.schedule.domain.ScheduleCandidate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScheduleCandidateReadAllResponseMapper {

    ScheduleCandidateReadAllResponseMapper INSTANCE = Mappers.getMapper(ScheduleCandidateReadAllResponseMapper.class);

    @Mappings({
            @Mapping(target = "isConfirmed", source = "scheduleCandidate", qualifiedByName = "isConfirmed"),
            @Mapping(target = "scheduleId", source = "schedule.id"),
            @Mapping(target = "scheduleTitle", source = "schedule.title"),
            @Mapping(target = "scheduleDescription", source = "schedule.description"),
            @Mapping(target = "colorCode", source = "schedule.colorCode")
    })
    ScheduleCandidateReadAllResponse map(ScheduleCandidate scheduleCandidate);

    @Named("isConfirmed")
    default boolean isConfirmed(ScheduleCandidate scheduleCandidate) {
        return scheduleCandidate.getSchedule().isConfirmed();
    }
}
