package com.postsquad.scoup.web.user.controller;

import com.postsquad.scoup.web.user.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/social/authenticate")
    public SocialAuthenticationResponse readUserData(@RequestParam String code) {
        return userService.readUserData(code);
    }

    @GetMapping("/social/authenticate/token")
    public SocialAuthenticationResponse readUserDataWithToken(@RequestHeader("Authorization") String header) {
        return userService.readUserDataWithToken(header);
    }
}
