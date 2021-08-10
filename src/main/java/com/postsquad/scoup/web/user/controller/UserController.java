package com.postsquad.scoup.web.user.controller;

import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/validate/email")
    public EmailValidationResponse validateEmail(@RequestParam String email) {
//        return EmailValidationResponse.builder().existingEmail(true).build();
        return userService.validateEmail(email);
    }
}
