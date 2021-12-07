package com.postsquad.scoup.web.user.controller;

import com.postsquad.scoup.web.common.DefaultPostResponse;
import com.postsquad.scoup.web.common.QueryParam;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.user.controller.request.EmailValidationRequest;
import com.postsquad.scoup.web.user.controller.request.NicknameValidationRequest;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.controller.response.NicknameValidationResponse;
import com.postsquad.scoup.web.user.service.SignUpFailedException;
import com.postsquad.scoup.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // FIXME: https://github.com/Sc0up/scoup-backend/issues/18 논의 결과에 따라 수정 필요
    public DefaultPostResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return userService.signUp(signUpRequest);
    }

    @ExceptionHandler(SignUpFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse signUpFailedExceptionHandler(SignUpFailedException signUpFailedException) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, signUpFailedException.getMessage(), signUpFailedException.description());
    }

    @GetMapping("/validate/email")
    public EmailValidationResponse validateEmail(@QueryParam @Valid EmailValidationRequest emailValidationRequest) {
        return userService.validateEmail(emailValidationRequest);
    }

    @GetMapping("/validate/nickname")
    public NicknameValidationResponse validateNickname(@QueryParam @Valid NicknameValidationRequest nicknameValidationRequest) {
        return userService.validateNickname(nicknameValidationRequest);
    }
}
