package com.postsquad.scoup.web.signin.controller;

import com.postsquad.scoup.web.signin.controller.request.SignInRequest;
import com.postsquad.scoup.web.signin.controller.response.SignInResponse;
import com.postsquad.scoup.web.signin.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/sign-in")
@RestController
public class SignInController {

    private final SignInService signInService;

    @PostMapping
    public SignInResponse signIn(@RequestBody @Valid SignInRequest signInRequest, HttpServletResponse response) {
        SignInResponse signInResponse = signInService.signIn(signInRequest);

        Cookie cookie = new Cookie("refresh", signInResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/api"); // TODO: 로그인 이후 동작에서 잘 작동하는지 검증 필요

        response.addCookie(cookie);

        return signInResponse;
    }
}
