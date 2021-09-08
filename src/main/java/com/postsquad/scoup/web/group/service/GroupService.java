package com.postsquad.scoup.web.group.service;

import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.controller.request.GroupModificationRequest;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.group.exception.GroupNameAlreadyExistException;
import com.postsquad.scoup.web.group.exception.GroupNotFoundException;
import com.postsquad.scoup.web.group.mapper.GroupMapper;
import com.postsquad.scoup.web.group.repository.GroupRepository;
import com.postsquad.scoup.web.signin.exception.UnauthorizedUserException;
import com.postsquad.scoup.web.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public Long create(GroupCreationRequest groupCreationRequest, User user) {

        if (groupRepository.existsByName(groupCreationRequest.getName())) {
            throw new GroupNameAlreadyExistException(groupCreationRequest.getName());
        }

        Group group = GroupMapper.INSTANCE.groupCreationRequestToGroup(groupCreationRequest, user);

        return groupRepository.save(group).getId();
    }

    public Long update(Long groupId, GroupModificationRequest groupModificationRequest, User user) {

        if (groupRepository.existsByName(groupModificationRequest.getName())) {
            throw new GroupNameAlreadyExistException(groupModificationRequest.getName());
        }

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
        if (!group.getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedUserException();
        }
        return groupRepository.save(group.update(groupModificationRequest)).getId();
    }
}
