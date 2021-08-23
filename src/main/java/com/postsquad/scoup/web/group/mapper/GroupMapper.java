package com.postsquad.scoup.web.group.mapper;

import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.domain.Group;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    Group groupCreationRequestToGroup(GroupCreationRequest groupCreationRequest);
}
