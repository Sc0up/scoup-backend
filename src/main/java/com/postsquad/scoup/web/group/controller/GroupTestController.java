package com.postsquad.scoup.web.group.controller;

import com.postsquad.scoup.web.group.controller.response.GroupValidationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupTestController {

    @GetMapping("/test")
    public GroupValidationResponse test() {
        return GroupValidationResponse.builder()
                .isExistingName(true)
                .build();
    }
}
