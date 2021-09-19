package com.postsquad.scoup.web.group.provider;

import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Arrays;
import java.util.stream.Stream;

public class CreateGroupWithExistingNameProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "실패 - 이미 존재하는 그룹 이름",
                        GroupCreationRequest.builder()
                                            .name("name")
                                            .description("description")
                                            .build(),
                        ErrorResponse.builder()
                                     .message("Failed to create group")
                                     .statusCode(400)
                                     .errors(Arrays.asList("Group name 'name' already exists"))
                                     .build()
                )
        );
    }
}
