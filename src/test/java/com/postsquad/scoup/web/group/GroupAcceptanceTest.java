package com.postsquad.scoup.web.group;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.group.repository.GroupRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

public class GroupAcceptanceTest extends AcceptanceTestBase {

    @Autowired
    GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        groupRepository.save(Group.builder()
                                  .name("name")
                                  .description("description")
                                  .build());
    }

    @ParameterizedTest
    @MethodSource("createGroupProvider")
    @DisplayName("사용자가 새로운 그룹을 생성할 수 있다.")
    void createGroup(String description, GroupCreationRequest givenGroupCreationRequest, Group expectedGroup) {
        // given
        String path = "/api/groups";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .body(givenGroupCreationRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .log().all()
                                              .post();

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.CREATED.value());
        then(groupRepository.findById(actualResponse.body().as(Long.class)).orElse(null))
                .as("그룹 생성: %s", description)
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForResponseWithId)
                .isEqualTo(expectedGroup);
    }

    static Stream<Arguments> createGroupProvider() {
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
                             .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createGroupWithExistingNameProvider")
    @DisplayName("중복된 그룹 이름이 있으면 그룹 생성을 할 수 없다.")
    void createGroupWithExistingName(String description, GroupCreationRequest givenGroupCreationRequest, ErrorResponse expectedResponse) {
        // given
        String path = "/api/groups";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .body(givenGroupCreationRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .log().all()
                                              .post();

        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.BAD_REQUEST.value());
        then(actualResponse.as(ErrorResponse.class))
                .as("그룹 생성: %s", description)
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForErrorResponse)
                .isEqualTo(expectedResponse);
    }

    static Stream<Arguments> createGroupWithExistingNameProvider() {
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

    @ParameterizedTest
    @MethodSource("validateGroupCreationRequestProvider")
    @DisplayName("GroupCreationRequest validation")
    void validateGroupCreationRequest(String description, GroupCreationRequest givenGroupCreationRequest, ErrorResponse expectedResponse) {
        // given
        String path = "/api/groups";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .body(givenGroupCreationRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .log().all()
                                              .post();

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.BAD_REQUEST.value());
        then(actualResponse.as(ErrorResponse.class))
                .as(description)
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForErrorResponse)
                .isEqualTo(expectedResponse);
    }

    static Stream<Arguments> validateGroupCreationRequestProvider() {
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
