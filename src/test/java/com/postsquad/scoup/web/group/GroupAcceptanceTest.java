package com.postsquad.scoup.web.group;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.group.repository.GroupRepository;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    // temporary token with sub(userId) = 1, exp = 2023-01-01T00:00:00.000(Korean Standard Time)
    private static String TEST_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjcyNDk4ODAwfQ.DXojMeUGIq77XWvQK0luZtZhsi-c6s9qjiiu9vHhkbg";
    private static User TEST_USER = User.builder()
                                        .nickname("nickname")
                                        .email("email@email.com")
                                        .password("password")
                                        .avatarUrl("url")
                                        .username("username")
                                        .build();

    @BeforeEach
    void setUp() {
        groupRepository.save(Group.builder()
                                  .name("name")
                                  .description("description")
                                  .build());
        userRepository.save(TEST_USER);
    }

    @AfterEach
    void tearDown() {
        groupRepository.deleteAll();
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
                                                       .header("Accept-Language", "en-US")
                                                       .header("Authorization", TEST_TOKEN)
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
                             .owner(TEST_USER)
                             .build(),
                        TEST_USER
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
                                                       .header("Authorization", TEST_TOKEN)
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
                                                       .header("Authorization", TEST_TOKEN)
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

    // 일단 엔티티에 오너 추가
    // 그룹 생성시에 토큰에서 유저아이디 꺼내서 오너 등록
    // 토큰에 있는 유저 아이디로 유저 조회
    // 수정할 그룹의 오너인지 확인
    // 그룹 수정
//    @ParameterizedTest
//    @MethodSource("modifyGroupProvider")
//    @DisplayName("사용자가 그룹을 수정 할 수 있다")
//    void modifyGroup(String description, GroupModificationRequest givenGroupModificationRequest, Group expectedGroup) {
//        // given
//        // TODO: 헤더에 토큰 필요
//        String path = "/api/group";
//        RequestSpecification givenRequest = RestAssured.given()
//                                                       .baseUri(BASE_URL)
//                                                       .port(port)
//                                                       .basePath(path)
//                                                       .contentType(ContentType.JSON)
//                                                       .header("Accept-Language", "en-US")
//                                                       .header("Authorization", TEST_TOKEN)
//                                                       .body(givenGroupModificationRequest);
//
//        // when
//        Response actualResponse = givenRequest.when()
//                                              .log().all()
//                                              .put();
//
//        // then
//        // TODO: 뭘 리턴하지? 일단은 수정된 그룹의 아이디를 리턴한다고 가정
//        actualResponse.then()
//                      .log().all()
//                      .statusCode(HttpStatus.NO_CONTENT.value());
//        then(groupRepository.findById(actualResponse.as(Long.class)).orElse(null))
//                .as("그룹 수정: %s" + description)
//                .usingRecursiveComparison()
//                .ignoringFields(ignoringFieldsForResponse)
//                .isEqualTo(expectedGroup);
//    }
//
//    static Stream<Arguments> modifyGroupProvider() {
//        return Stream.of(
//                Arguments.of(
//                        "성공",
//                        GroupModificationRequest.builder()
//                                                .name("modifiedName")
//                                                .description("modified description")
//                                                .build(),
//                        Group.builder()
//                             .name("modifiedName")
//                             .description("modified description")
//                             .build()
//                )
//        );
//    }
}
