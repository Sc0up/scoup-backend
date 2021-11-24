package com.postsquad.scoup.web.group.controller;

import com.postsquad.scoup.web.group.controller.response.GroupMemberReadAllResponse;
import com.postsquad.scoup.web.group.controller.response.GroupMemberReadAllResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
