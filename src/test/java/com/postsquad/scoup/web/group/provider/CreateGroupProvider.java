package com.postsquad.scoup.web.group.provider;

import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;


public class CreateGroupProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "성공",
                        GroupCreationRequest.builder()
                                            .name("group name")
                                            .description("description")
                                            .build(),
                        Group.builder()
                             .name("group name")
                             .description("description")
                             .owner(User.builder()
                                        .nickname("nickname")
                                        .email("email@email.com")
                                        .password("password")
                                        .avatarUrl("url")
                                        .username("username")
                                        .oAuthUsers(List.of(OAuthUser.of(OAuthType.NONE, "")))
                                        .build())
                             .build()
                )
        );
    }
}
