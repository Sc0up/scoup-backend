package com.postsquad.scoup.web.auth.mapper;

import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.auth.controller.response.GitHubUserResponse;
import com.postsquad.scoup.web.auth.controller.response.GoogleUserResponse;
import com.postsquad.scoup.web.auth.controller.response.KakaoUserResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SocialAuthenticationResponseMapper {

    SocialAuthenticationResponseMapper INSTANCE = Mappers.getMapper(SocialAuthenticationResponseMapper.class);

    @Mappings({
            @Mapping(target = "oauthType", source = "type"),
            @Mapping(target = "socialServiceId", source = "gitHubUserResponse.id"),
            @Mapping(target = "name", source = "gitHubUserResponse.name"),
            @Mapping(target = "email", source = "gitHubUserResponse.email"),
            @Mapping(target = "avatarUrl", source = "gitHubUserResponse.avatarUrl")
    })
    SocialAuthenticationResponse map(GitHubUserResponse gitHubUserResponse, OAuthType type);

    @Mappings({
            @Mapping(target = "oauthType", source = "type"),
            @Mapping(target = "socialServiceId", source = "kakaoUserResponse.id"),
            @Mapping(target = "name", source = "kakaoUserResponse.kakaoAccount.profile.nickname"),
            @Mapping(target = "email", source = "kakaoUserResponse.kakaoAccount.email"),
            @Mapping(target = "avatarUrl", source = "kakaoUserResponse.kakaoAccount.profile.profileImageUrl")
    })
    SocialAuthenticationResponse map(KakaoUserResponse kakaoUserResponse, OAuthType type);

    @Mappings({
            @Mapping(target = "oauthType", source = "type"),
            @Mapping(target = "socialServiceId", source = "googleUserResponse.sub"),
            @Mapping(target = "name", source = "googleUserResponse.name"),
            @Mapping(target = "email", source = "googleUserResponse.email"),
            @Mapping(target = "avatarUrl", source = "googleUserResponse.picture")
    })
    SocialAuthenticationResponse map(GoogleUserResponse googleUserResponse, OAuthType type);
}
