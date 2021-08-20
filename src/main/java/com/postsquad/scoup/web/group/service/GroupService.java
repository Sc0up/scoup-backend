package com.postsquad.scoup.web.group.service;

import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.group.mapper.GroupMapper;
import com.postsquad.scoup.web.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public Long create(GroupCreationRequest groupCreationRequest) {
        Group group = GroupMapper.INSTANCE.groupCreationRequestToGroup(groupCreationRequest);

        return groupRepository.save(group).getId();
    }
}
