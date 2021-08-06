package com.postsquad.scoup.web.user.controller;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signUp(@RequestBody SignUpRequest signUpRequest) {
        System.out.println(signUpRequest);
    }
}
