package com.postsquad.scoup.web.group.controller;

import com.postsquad.scoup.web.group.controller.request.GroupMemberCreationRequest;
import com.postsquad.scoup.web.group.controller.response.GroupMemberReadAllResponse;
import com.postsquad.scoup.web.group.controller.response.GroupMemberReadAllResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupMemberController {

    @GetMapping("/groups/{groupId}/members")
    public GroupMemberReadAllResponses readAll(@PathVariable long groupId) {
        return GroupMemberReadAllResponses.from(List.of(
                GroupMemberReadAllResponse.builder()
                                          .userId(1L)
                                          .nickname("nickname")
                                          .email("email")
                                          .avatarUrl("avatarUrl")
                                          .build()
        ));
    }

    @PostMapping("/groups/{groupId}/members/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@PathVariable long groupId, @RequestBody GroupMemberCreationRequest groupMemberCreationRequest) {
    }
}
