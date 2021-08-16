package com.postsquad.scoup.web.group.controller;

import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/groups")
@RestController
public class GroupController {

    @GetMapping
    public Long create(@RequestBody GroupCreationRequest groupCreationRequest) {
        // dummy
        return 1L;
    }
}
