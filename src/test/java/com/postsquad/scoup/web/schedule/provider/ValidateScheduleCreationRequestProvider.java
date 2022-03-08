package com.postsquad.scoup.web.schedule.provider;

import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCandidateCreationRequest;
import com.postsquad.scoup.web.schedule.controller.request.ScheduleCreationRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ValidateScheduleCreationRequestProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "실패",
                        ScheduleCreationRequest.builder()
                                .title(null)
                                .description("description")
                                .pollDueDateTime(LocalDateTime.of(21, 11, 27, 0, 0))
                                .isPollAnonymous(true)
                                .scheduleCandidates(List.of(ScheduleCandidateCreationRequest.builder()
                                        .startDateTime(LocalDateTime.of(21, 11, 25, 0, 0))
                                        .endDateTime(LocalDateTime.of(21, 11, 26, 0, 0))
                                        .build()))
                                .build(),
                        ErrorResponse.builder()
                                .message("Method argument not valid.")
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .errors(Collections.singletonList("title: must not be empty"))
                                .build()
                ),
                Arguments.of(
                        "실패",
                        ScheduleCreationRequest.builder()
                                .title("")
                                .description("description")
                                .pollDueDateTime(LocalDateTime.of(21, 11, 27, 0, 0))
                                .isPollAnonymous(true)
                                .scheduleCandidates(List.of(ScheduleCandidateCreationRequest.builder()
                                                                                            .startDateTime(LocalDateTime.of(21, 11, 25, 0, 0))
                                                                                            .endDateTime(LocalDateTime.of(21, 11, 26, 0, 0))
                                                                                            .build()))
                                .build(),
                        ErrorResponse.builder()
                                .message("Method argument not valid.")
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .errors(Collections.singletonList("title: must not be empty"))
                                .build()
                ),
                Arguments.of(
                        "실패",
                        ScheduleCreationRequest.builder()
                                .title("title")
                                .description("description")
                                .pollDueDateTime(LocalDateTime.of(21, 11, 27, 0, 0))
                                .isPollAnonymous(true)
                                .scheduleCandidates(List.of())
                                .build(),
                        ErrorResponse.builder()
                                .message("Method argument not valid.")
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .errors(Collections.singletonList("scheduleCandidates: size must be between 1 and 20"))
                                .build()
                )
        );
    }
}
