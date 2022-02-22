package com.postsquad.scoup.web.schedule.mapper;

import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCreationRequest;
import com.postsquad.scoup.web.schedule.domain.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScheduleMapper {

    ScheduleMapper INSTANCE = Mappers.getMapper(ScheduleMapper.class);

    @Mappings({ // mapping으로 변경
            @Mapping(target = "group", source = "groupId", qualifiedByName = "group")
    })
    Schedule map(Long groupId, ScheduleCreationRequest scheduleCreationRequest);

    @Named("group")
    default Group group(long groupId) {
        Group group = Group.builder().build();
        group.setId(groupId);
        return group;
    }
}
