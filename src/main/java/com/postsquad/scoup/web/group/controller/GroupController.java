package com.postsquad.scoup.web.group.controller;

import com.postsquad.scoup.web.common.DefaultPostResponse;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.controller.request.GroupModificationRequest;
import com.postsquad.scoup.web.group.controller.request.GroupValidationRequest;
import com.postsquad.scoup.web.group.controller.response.GroupReadOneResponse;
import com.postsquad.scoup.web.group.controller.response.GroupValidationResponse;
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

    @GetMapping("/{groupId}")
    public GroupReadOneResponse readOne(@PathVariable long groupId) {
        // TODO: GroupService.readOne
        return GroupReadOneResponse.builder()
                                   .id(1L)
                                   .image("image")
                                   .name("name")
                                   .description("description")
                                   .build();
    }


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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long groupId, @RequestBody @Valid GroupModificationRequest groupModificationRequest, @LoggedInUser User user) {
        groupService.update(groupId, groupModificationRequest, user);
    }

    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long groupId) {
        // TODO: 삭제 서비스, 사용자 인증
    }

    @GetMapping("/validate/group-name")
    public GroupValidationResponse validateGroupName(@Valid GroupValidationRequest groupValidationRequest) {
        return GroupValidationResponse.builder()
                                      .isExistingName(true)
                                      .build();
    }
}
