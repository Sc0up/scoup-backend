package com.postsquad.scoup.web.group.repository;

import com.postsquad.scoup.web.group.domain.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
public class GroupRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Test
    void save() {
        // given
        Group group = Group.builder()
                           .name("name")
                           .description("description")
                           .build();

        // when
        groupRepository.save(group);

        // then
        then(groupRepository.findById(group.getId()).get())
                .isEqualTo(group);
    }

    @ParameterizedTest
    @MethodSource("columnConstraintViolationProvider")
    void databaseConstraintViolation(String description, Group givenGroup) {
        // when
        Throwable actualExceptionThrown = catchThrowable(() -> groupRepository.save(givenGroup));

        // then
        then(actualExceptionThrown)
                .as("컬럼 제약 조건 위반: ")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    static Stream<Arguments> columnConstraintViolationProvider() {
        return Stream.of(
                Arguments.of(
                        "이름 없음",
                        Group.builder()
                        .description("description")
                        .build()
                ), Arguments.of(
                        "이름 길이 초과",
                        Group.builder()
                        .name("this is a group name with length violation")
                        .description("description")
                        .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("tableConstraintViolationProvider")
    void constraintViolation(String description, Group givenGroup, Group givenGroupWithSameName) {
        // when
        groupRepository.save(givenGroup);
        Throwable actualExceptionThrown = catchThrowable(() -> groupRepository.save(givenGroupWithSameName));

        // then
        then(actualExceptionThrown)
                .as("테이블 제약 조건 위반: ")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    static Stream<Arguments> tableConstraintViolationProvider() {
        return Stream.of(
                Arguments.of(
                        "이름 중복",
                        Group.builder()
                             .name("name")
                             .description("")
                             .build(),
                        Group.builder()
                             .name("name")
                             .description("")
                             .build()
                )
        );
    }
}
