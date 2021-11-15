package com.postsquad.scoup.web.schedule.provider;

import com.postsquad.scoup.web.schedule.controller.response.ConfirmedParticipantResponse;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleReadAllResponse;
import com.postsquad.scoup.web.schedule.controller.response.ConfirmedScheduleReadAllResponses;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class ConfirmedScheduleReadAllProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "성공",
                        1L,
                        ConfirmedScheduleReadAllResponses.from(List.of(ConfirmedScheduleReadAllResponse.builder()
                                                                                                       .startDateTime(LocalDateTime.of(2021, 9, 25, 9, 0))
                                                                                                       .endDateTime(LocalDateTime.of(2021, 9, 25, 11, 0))
                                                                                                       .title("schedule title")
                                                                                                       .description("schedule description")
                                                                                                       .confirmedParticipants(List.of(
                                                                                                               ConfirmedParticipantResponse.builder()
                                                                                                                                           .nickname("nickname")
                                                                                                                                           .username("username")
                                                                                                                                           .build(),
                                                                                                               ConfirmedParticipantResponse.builder()
                                                                                                                                           .nickname("nickname2")
                                                                                                                                           .username("username2")
                                                                                                                                           .build())

                                                                                                       )
                                                                                                       .build()))
                )
        );
    }
}
