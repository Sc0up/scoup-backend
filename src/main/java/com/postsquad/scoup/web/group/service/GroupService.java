package com.postsquad.scoup.web.group.service;

import com.postsquad.scoup.web.common.DefaultPostResponse;
import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.controller.request.GroupModificationRequest;
import com.postsquad.scoup.web.group.controller.response.GroupReadAllResponses;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.group.exception.GroupNameAlreadyExistException;
import com.postsquad.scoup.web.group.exception.GroupNotFoundException;
import com.postsquad.scoup.web.group.mapper.GroupMapper;
import com.postsquad.scoup.web.group.repository.GroupRepository;
import com.postsquad.scoup.web.signin.exception.UnauthorizedUserException;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;

    public DefaultPostResponse create(GroupCreationRequest groupCreationRequest, User user) {

        if (groupRepository.existsByName(groupCreationRequest.getName())) {
            throw new GroupNameAlreadyExistException(groupCreationRequest.getName());
        }

        User owner = userService.findById(user.getId());
        Group group = GroupMapper.INSTANCE.map(groupCreationRequest, owner);
        group.addMember(owner);

        return DefaultPostResponse.builder().id(groupRepository.save(group).getId()).build();
    }

    public Long update(Long groupId, GroupModificationRequest groupModificationRequest, User user) {

        if (groupRepository.existsByName(groupModificationRequest.getName())) {
            throw new GroupNameAlreadyExistException(groupModificationRequest.getName());
        }

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
        if (group.verifyOwner(user)) {
            throw new UnauthorizedUserException();
        }
        return groupRepository.save(group.update(groupModificationRequest)).getId();
    }

    public GroupReadAllResponses readAllByUser(User user) {
        User loggedInUser = userService.findById(user.getId());
        return GroupReadAllResponses
                .builder()
                .groupReadAllResponse(
                        loggedInUser.getJoinedGroups().stream()
                                .map(GroupMapper.INSTANCE::map)
                                .collect(Collectors.toList()))
                .build();
    }
}
