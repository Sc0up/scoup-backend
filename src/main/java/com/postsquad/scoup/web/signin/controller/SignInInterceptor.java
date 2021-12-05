package com.postsquad.scoup.web.signin.controller;

import com.nimbusds.jwt.SignedJWT;
import com.postsquad.scoup.web.signin.exception.AuthorizationFailedException;
import com.postsquad.scoup.web.signin.exception.UserNotFoundException;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SignInInterceptor implements HandlerInterceptor {

    private static final List<String> PATH_TO_INCLUDE = List.of(
            "/groups/**"
    );

    private static final List<String> PATH_TO_EXCLUDE = List.of();

    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            throw new AuthorizationFailedException("Sign in required");
        }

        String token = authorization.substring("Bearer ".length());
        Long userId = Long.parseLong(SignedJWT.parse(token).getJWTClaimsSet().getSubject());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        request.setAttribute("user", user);

        return true;
    }

    public List<String> pathToInclude() {
        return PATH_TO_INCLUDE;
    }

    public List<String> pathToExclude() {
        return PATH_TO_EXCLUDE;
    }
}
