package com.postsquad.scoup.web.group.controller;

import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.exception.GroupCreationFailedException;
import com.postsquad.scoup.web.group.service.GroupService;
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
    public Long create(@RequestBody @Valid GroupCreationRequest groupCreationRequest) {
        return groupService.create(groupCreationRequest);
    }

    @ExceptionHandler(GroupCreationFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse groupCreationFailedExceptionHandler(GroupCreationFailedException groupCreationFailedException) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, groupCreationFailedException.getMessage(), groupCreationFailedException.getDescription());
    }
}
