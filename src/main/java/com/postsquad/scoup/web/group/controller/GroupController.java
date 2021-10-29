package com.postsquad.scoup.web.group.controller;

import com.postsquad.scoup.web.common.DefaultPostResponse;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.controller.request.GroupModificationRequest;
import com.postsquad.scoup.web.group.exception.GroupCreationFailedException;
import com.postsquad.scoup.web.group.service.GroupService;
import com.postsquad.scoup.web.user.LoggedInUser;
import com.postsquad.scoup.web.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/groups")
@RestController
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DefaultPostResponse create(@RequestBody @Valid GroupCreationRequest groupCreationRequest, @LoggedInUser User user) {
        return groupService.create(groupCreationRequest, user);
    }

    @ExceptionHandler(GroupCreationFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse groupCreationFailedExceptionHandler(GroupCreationFailedException groupCreationFailedException) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, groupCreationFailedException.getMessage(), groupCreationFailedException.getDescription());
    }

    @PutMapping("/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    // FIXME: 204로 변경되어야함.
    public Long update(@PathVariable Long groupId, @RequestBody @Valid GroupModificationRequest groupModificationRequest, @LoggedInUser User user) {
        return groupService.update(groupId, groupModificationRequest, user);
    }
}
