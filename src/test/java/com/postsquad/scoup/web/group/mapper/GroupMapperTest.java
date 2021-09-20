package com.postsquad.scoup.web.group.mapper;

import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.user.domain.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

public class GroupMapperTest {

    private static final User TEST_OWNER = User.builder()
                                               .nickname("nickname")
                                               .username("username")
                                               .email("email@email.com")
                                               .avatarUrl("avatarUrl")
                                               .password("password")
                                               .build();

    @ParameterizedTest
    @MethodSource("groupCreationRequestToGroupProvider")
    void groupCreationRequestToGroup(GroupCreationRequest givenGroupCreationRequest, Group expectedGroup) {
        // when
        Group group = GroupMapper.INSTANCE.map(givenGroupCreationRequest, TEST_OWNER);

        // then
        then(group).usingRecursiveComparison()
                   .isEqualTo(expectedGroup);
    }

    static Stream<Arguments> groupCreationRequestToGroupProvider() {
        return Stream.of(
                Arguments.of(
                        GroupCreationRequest.builder()
                                            .name("name")
                                            .description("description")
                                            .build(),
                        Group.builder()
                             .name("name")
                             .description("description")
                             .owner(TEST_OWNER)
                             .build()
                )
        );
    }
}
