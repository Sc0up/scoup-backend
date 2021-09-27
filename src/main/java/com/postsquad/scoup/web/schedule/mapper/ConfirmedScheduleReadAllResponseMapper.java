package com.postsquad.scoup.web.schedule.mapper;

import com.postsquad.scoup.web.schedule.controller.response.ConfirmedParticipantResponse;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleReadAllResponse;
import com.postsquad.scoup.web.schedule.domain.ConfirmedSchedule;
import com.postsquad.scoup.web.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ConfirmedScheduleReadAllResponseMapper {

    ConfirmedScheduleReadAllResponseMapper INSTANCE = Mappers.getMapper(ConfirmedScheduleReadAllResponseMapper.class);

    @Mappings({
            @Mapping(target = "title", source = "schedule.title"),
            @Mapping(target = "description", source = "schedule.description")
    })
    ConfirmedScheduleReadAllResponse map(ConfirmedSchedule confirmedSchedule);

    List<ConfirmedParticipantResponse> map(List<User> confirmedParticipants);

    ConfirmedParticipantResponse map(User user);
}
