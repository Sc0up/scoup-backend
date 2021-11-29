package com.postsquad.scoup.web.group.provider;

import com.postsquad.scoup.web.group.domain.Group;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class DeleteGroupProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "성공",
                        Group.builder()
                             .name("groupToBeDeleted")
                             .description("description")
                             .build()
                )
        );
    }
}
