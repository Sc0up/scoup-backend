package com.postsquad.scoup.web.group;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.TestEntityManager;
import com.postsquad.scoup.web.common.DefaultPostResponse;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.group.controller.request.GroupCreationRequest;
import com.postsquad.scoup.web.group.controller.request.GroupModificationRequest;
import com.postsquad.scoup.web.group.controller.response.GroupReadOneResponse;
import com.postsquad.scoup.web.group.controller.response.GroupValidationResponse;
import com.postsquad.scoup.web.group.domain.Group;
import com.postsquad.scoup.web.group.provider.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import java.util.ArrayList;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class GroupAcceptanceTest extends AcceptanceTestBase {

    private static final Snippet GROUP_READ_ONE_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID")
    );

    private static final Snippet GROUP_READ_ONE_RESPONSE_FIELDS = responseFields(
            fieldWithPath("id")
                    .type(JsonFieldType.NUMBER)
                    .description("id"),
            fieldWithPath("name")
                    .type(JsonFieldType.STRING)
                    .description("이름"),
            fieldWithPath("description")
                    .type(JsonFieldType.STRING)
                    .description("설명"),
            fieldWithPath("image")
                    .type(JsonFieldType.STRING)
                    .description("이미지")
    );

    private static final Snippet GROUP_CREATION_REQUEST_FIELDS = requestFields(
            fieldWithPathAndConstraints("name", GroupCreationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("그룹 명"),
            fieldWithPathAndConstraints("description", GroupCreationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("그룹 설명")
                    .optional()
    );

    private static final Snippet GROUP_MODIFICATION_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID")
    );

    private static final Snippet GROUP_MODIFICATION_REQUEST_FIELDS = requestFields(
            fieldWithPathAndConstraints("name", GroupCreationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("그룹 명"),
            fieldWithPathAndConstraints("description", GroupCreationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("그룹 설명")
                    .optional()
    );

    private static final Snippet GROUP_DELETION_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID")
    );

    private static final Snippet GROUP_NAME_VALIDATION_REQUEST_FIELDS = requestParameters(
            parameterWithName("group_name")
                    .description("중복 체크할 그룹 명")
    );

    private static final Snippet GROUP_NAME_VALIDATION_RESPONSE_FIELDS = responseFields(
            fieldWithPath("is_existing_name")
                    .type(JsonFieldType.BOOLEAN)
                    .description("그룹 명 중복 여부")
    );

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        testEntityManager.persist(testUser);
    }

    @Test
    @DisplayName("선택한 그룹 정보를 조회할 수 있다.(이미 지나간 일정은 보이면 안 됨)")
    void readOne() {
        // given
        Group givenGroup = Group.builder()
                                .name("name")
                                .description("description")
                                // TODO: .image("image")
                                .build();
        testEntityManager.persist(givenGroup);
        GroupReadOneResponse expectedGroupReadOneResponse = GroupReadOneResponse.builder()
                                                                                .id(1L)
                                                                                .image("image")
                                                                                .name("name")
                                                                                .description("description")
                                                                                .build();
        String path = "/groups/{groupId}";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", givenGroup.getId());

        //when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      GROUP_READ_ONE_PATH_PARAMETERS,
                                                      GROUP_READ_ONE_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .get(path);

        //then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());
        then(actualResponse.as(GroupReadOneResponse.class))
                // TODO: Add description
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForResponseWithId)
                .isEqualTo(expectedGroupReadOneResponse);
    }

    @ParameterizedTest
    @ArgumentsSource(CreateGroupProvider.class)
    @DisplayName("사용자가 새로운 그룹을 생성할 수 있다")
    void createGroup(String description, GroupCreationRequest givenGroupCreationRequest, Group expectedGroup) {
        // given
        String path = "/api/groups";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .header("Authorization", TEST_TOKEN)
                                                       .body(givenGroupCreationRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      GROUP_CREATION_REQUEST_FIELDS,
                                                      DEFAULT_POST_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .post();

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.CREATED.value());
        testEntityManager.findAndConsume(Group.class, actualResponse.body().as(DefaultPostResponse.class).getId(),
                                         actualGroup -> {
                                             then(actualGroup)
                                                     .as("그룹 생성: %s", description)
                                                     .usingRecursiveComparison()
                                                     .ignoringFields(ignoringFieldsForResponseWithId)
                                                     .ignoringFields("owner")
                                                     .isEqualTo(expectedGroup);
                                             then(actualGroup.getOwner())
                                                     .usingRecursiveComparison()
                                                     .ignoringFields(ignoringFieldsForResponseWithId)
                                                     .isEqualTo(expectedGroup.getOwner());
                                         }
        );
    }

    @ParameterizedTest
    @ArgumentsSource(CreateGroupWithExistingNameProvider.class)
    @DisplayName("중복된 그룹 이름이 있으면 그룹 생성을 할 수 없다")
    void createGroupWithExistingName(String description, GroupCreationRequest givenGroupCreationRequest, ErrorResponse expectedResponse) {
        // given
        testEntityManager.persist(Group.builder().name("name").description("").build());
        String path = "/api/groups";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .header("Authorization", TEST_TOKEN)
                                                       .body(givenGroupCreationRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      GROUP_CREATION_REQUEST_FIELDS,
                                                      ERROR_RESPONSE_FIELDS
                                              ))
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

    @ParameterizedTest
    @ArgumentsSource(ValidateGroupCreationRequestProvider.class)
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

    @ParameterizedTest
    @ArgumentsSource(ModifyGroupProvider.class)
    @DisplayName("사용자가 그룹을 수정 할 수 있다")
    void modifyGroup(String description, Long givenGroupId, GroupModificationRequest givenGroupModificationRequest, Group expectedGroup) {
        // given
        Group group = Group.builder().name("name").description("").owner(testUser).schedules(new ArrayList<>()).build();
        testEntityManager.persist(group);
        String path = "/groups/{groupId}";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .pathParam("groupId", givenGroupId)
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .header("Authorization", TEST_TOKEN)
                                                       .body(givenGroupModificationRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      GROUP_MODIFICATION_PATH_PARAMETERS,
                                                      GROUP_MODIFICATION_REQUEST_FIELDS
                                              ))
                                              .log().all()
                                              .put(path);

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.NO_CONTENT.value());
        testEntityManager.findAndConsume(Group.class, givenGroupId,
                                         actualGroup -> then(actualGroup)
                                                 .as("그룹 수정: %s", description)
                                                 .usingRecursiveComparison()
                                                 .ignoringFields(ignoringFieldsForResponseWithId)
                                                 .ignoringFields("owner")
                                                 .isEqualTo(expectedGroup)
        );
    }

    @ParameterizedTest
    @ArgumentsSource(DeleteGroupProvider.class)
    @DisplayName("사용자가 그룹을 삭제 할 수 있다")
    void deleteGroup(String description, Group givenGroup) {
        testEntityManager.persist(givenGroup);

        String path = "/groups/{groupId}";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .pathParam("groupId", givenGroup.getId())
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .header("Authorization", TEST_TOKEN);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      GROUP_DELETION_PATH_PARAMETERS
                                              ))
                                              .log().all()
                                              .delete(path);

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.NO_CONTENT.value());
        // TODO: DB에 해당 그룹 존재하지 않는 것 확인
    }

    @Test
    @DisplayName("이미 가입된 그룹 이름을 입력할 경우 그룹 이름이 중복되었다는 메시지가 반환된다.")
    void validateGroupName() {
        // given
        Group givenGroup = Group.builder()
                                .name("name")
                                .description("description")
                                // TODO: .image("image")
                                .build();
        testEntityManager.persist(givenGroup);
        GroupValidationResponse expectedGroupValidationResponse = GroupValidationResponse.builder()
                                                                                         .isExistingName(true)
                                                                                         .build();
        String path = "/groups/validate/group-name";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .header("Authorization", TEST_TOKEN)
                                                       .queryParam("group_name", givenGroup.getName());

        //when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      GROUP_NAME_VALIDATION_REQUEST_FIELDS,
                                                      GROUP_NAME_VALIDATION_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .get(path);

        //then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());
        then(actualResponse.as(GroupValidationResponse.class))
                // TODO: Add description
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForResponseWithId)
                .isEqualTo(expectedGroupValidationResponse);
    }
}
