package com.postsquad.scoup.web.group.provider;

import com.postsquad.scoup.web.group.controller.response.GroupReadAllResponse;
import com.postsquad.scoup.web.group.controller.response.GroupReadAllResponses;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

public class GroupReadAllProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "성공",
                        GroupReadAllResponses.builder()
                                             .groupReadAllResponses(
                                                     List.of(
                                                             GroupReadAllResponse.builder().name("name").description("").build(),
                                                             GroupReadAllResponse.builder().name("group2").description("group2").build())
                                             )
                                             .build()
                )
        );
    }
}
