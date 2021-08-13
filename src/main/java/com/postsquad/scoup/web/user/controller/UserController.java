package com.postsquad.scoup.web.user.controller;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@RestController
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    // FIXME: https://github.com/Sc0up/scoup-backend/issues/18 논의 결과에 따라 수정 필요
    public long signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return userService.signIn(signUpRequest);
    }

    @GetMapping("/validate/email")
    public EmailValidationResponse validateEmail(@RequestParam @NotEmpty @Email String email) {
        return userService.validateEmail(email);
    }
}
