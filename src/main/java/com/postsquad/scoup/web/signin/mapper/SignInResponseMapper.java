package com.postsquad.scoup.web.signin.mapper;

import com.postsquad.scoup.web.signin.controller.response.SignInResponse;
import com.postsquad.scoup.web.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SignInResponseMapper {

    SignInResponseMapper INSTANCE = Mappers.getMapper(SignInResponseMapper.class);

    SignInResponse userToSignInResponse(User user, String accessToken);
}
