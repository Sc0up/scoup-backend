package com.postsquad.scoup.web.group.provider;

import com.postsquad.scoup.web.group.GroupAcceptanceTest;
import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.domain.Group;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static com.postsquad.scoup.web.group.GroupAcceptanceTest.TEST_USER;

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
                             .owner(TEST_USER)
                             .build()
                )
        );
    }
}
