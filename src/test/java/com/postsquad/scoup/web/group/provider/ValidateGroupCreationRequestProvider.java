package com.postsquad.scoup.web.group.provider;

import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.stream.Stream;

public class ValidateGroupCreationRequestProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "실패",
                        GroupCreationRequest.builder()
                                            .name("")
                                            .description("")
                                            .build(),
                        ErrorResponse.builder()
                                     .message("Method argument not valid.")
                                     .statusCode(HttpStatus.BAD_REQUEST.value())
                                     .errors(Collections.singletonList("name: must not be empty"))
                                     .build()
                )
        );
    }
}
