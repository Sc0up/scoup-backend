package com.postsquad.scoup.web.auth.controller.response;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SocialAuthenticationResponseMapper {

    SocialAuthenticationResponseMapper INSTANCE = Mappers.getMapper(SocialAuthenticationResponseMapper.class);

    @Mappings({
            @Mapping(target = "oAuthType", source = "type"),
            @Mapping(target = "socialServiceId", source = "gitHubUserResponse.id"),
            @Mapping(target = "username", source = "gitHubUserResponse.login"),
            @Mapping(target = "email", source = "gitHubUserResponse.email"),
            @Mapping(target = "avatarUrl", source = "gitHubUserResponse.avatarUrl")
    })
    SocialAuthenticationResponse gitHubUserResponseToSocialAuthenticationResponse(GitHubUserResponse gitHubUserResponse, String type);

    @Mappings({
            @Mapping(target = "oAuthType", source = "type"),
            @Mapping(target = "socialServiceId", source = "kakaoUserResponse.id"),
            @Mapping(target = "username", source = "kakaoUserResponse.kakaoAccount.profile.nickname"),
            @Mapping(target = "email", source = "kakaoUserResponse.kakaoAccount.email"),
            @Mapping(target = "avatarUrl", source = "kakaoUserResponse.kakaoAccount.profile.profileImageUrl")
    })
    SocialAuthenticationResponse kakaoUserResponseToSocialAuthenticationResponse(KakaoUserResponse kakaoUserResponse, String type);

    @Mappings({
            @Mapping(target = "oAuthType", source = "type"),
            @Mapping(target = "socialServiceId", source = "googleUserResponse.sub"),
            @Mapping(target = "username", source = "googleUserResponse.name"),
            @Mapping(target = "email", source = "googleUserResponse.email"),
            @Mapping(target = "avatarUrl", source = "googleUserResponse.picture")
    })
    SocialAuthenticationResponse googleUserResponseToSocialAuthenticationResponse(GoogleUserResponse googleUserResponse, String type);
}
