package com.postsquad.scoup.web.user.mapper;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User map(SignUpRequest signUpRequest);

    @AfterMapping
    default void map(@MappingTarget User.UserBuilder userBuilder, SignUpRequest signUpRequest) {
        userBuilder.oAuthUsers(List.of(OAuthUser.of(signUpRequest.getOauthType(), signUpRequest.getSocialServiceId())));
    }
}
