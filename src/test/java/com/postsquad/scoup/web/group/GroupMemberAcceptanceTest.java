package com.postsquad.scoup.web.group;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.TestEntityManager;
import com.postsquad.scoup.web.group.controller.request.GroupMemberCreationRequest;
import com.postsquad.scoup.web.group.controller.request.GroupMemberRoleModificationRequest;
import com.postsquad.scoup.web.group.controller.response.GroupMemberReadAllResponse;
import com.postsquad.scoup.web.group.controller.response.GroupMemberReadAllResponses;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class GroupMemberAcceptanceTest extends AcceptanceTestBase {

    private static final Snippet GROUP_MEMBER_READ_ALL_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID")
    );

    private static final Snippet GROUP_MEMBER_READ_ALL_RESPONSE_FIELDS = responseFields(
            fieldWithPath("group_members[]")
                    .type(JsonFieldType.ARRAY)
                    .description("그룹 멤버 목록"),
            fieldWithPath("group_members[].user_id")
                    .type(JsonFieldType.NUMBER)
                    .description("그룹 멤버인 사용자 ID"),
            fieldWithPath("group_members[].nickname")
                    .type(JsonFieldType.STRING)
                    .description("그룹 멤버인 사용자 별명"),
            fieldWithPath("group_members[].email")
                    .type(JsonFieldType.STRING)
                    .description("그룹 멤버인 사용자 이메일"),
            fieldWithPath("group_members[].avatar_url")
                    .type(JsonFieldType.STRING)
                    .description("그룹 멤버인 사용자 아바타 URL")
                    .optional()
    );

    private static final Snippet GROUP_MEMBER_CREATION_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID")
    );

    private static final Snippet GROUP_MEMBER_CREATION_REQUEST_FIELDS = requestFields(
            fieldWithPathAndConstraints("email", GroupMemberCreationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("이메일")
    );

    private static final Snippet GROUP_MEMBER_ROLE_MODIFICATION_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID"),
            parameterWithName("memberId")
                    .description("그룹 멤버 ID")
    );

    private static final Snippet GROUP_MEMBER_ROLE_MODIFICATION_REQUEST_FIELDS = requestFields(
            fieldWithPathAndConstraints("role", GroupMemberCreationRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("그룹 멤버 역할")
    );

    private static final Snippet GROUP_MEMBER_DELETION_PATH_PARAMETERS = pathParameters(
            parameterWithName("groupId")
                    .description("그룹 ID"),
            parameterWithName("memberId")
                    .description("그룹 멤버 ID")
    );

    @Autowired
    TestEntityManager testEntityManager;


    @Test
    void readAll() {
        // given
        GroupMemberReadAllResponses expectedGroupMemberReadAllResponses = GroupMemberReadAllResponses.from(List.of(
                GroupMemberReadAllResponse.builder()
                                          .userId(1L)
                                          .nickname("nickname")
                                          .email("email")
                                          .avatarUrl("avatarUrl")
                                          .build()
        ));
        testEntityManager.persist(testUser);
        String path = "/groups/{groupId}/members";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", 1L);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      GROUP_MEMBER_READ_ALL_PATH_PARAMETERS,
                                                      GROUP_MEMBER_READ_ALL_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .get(path);

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());

        then(actualResponse.as(GroupMemberReadAllResponses.class))
                .usingRecursiveComparison()
                .isEqualTo(expectedGroupMemberReadAllResponses);
    }

    @Test
    void create() {
        // given
        testEntityManager.persist(testUser);
        GroupMemberCreationRequest givenGroupMemberCreationRequest = GroupMemberCreationRequest.builder()
                                                                                               .email("email")
                                                                                               .build();
        String path = "/groups/{groupId}/members/email";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", 1L)
                                                       .body(givenGroupMemberCreationRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      GROUP_MEMBER_CREATION_PATH_PARAMETERS,
                                                      GROUP_MEMBER_CREATION_REQUEST_FIELDS,
                                                      DEFAULT_POST_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .post(path);

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.CREATED.value());

        // TODO: id로 조회하여 검증
    }

    @Test
    void modifyRole() {
        // given
        testEntityManager.persist(testUser);
        GroupMemberRoleModificationRequest givenGroupMemberRoleModificationRequest = GroupMemberRoleModificationRequest.builder()
                                                                                                                       .role("role")
                                                                                                                       .build();
        String path = "/groups/{groupId}/members/{memberId}/role";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", 1L)
                                                       .pathParam("memberId", 1L)
                                                       .body(givenGroupMemberRoleModificationRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      GROUP_MEMBER_ROLE_MODIFICATION_PATH_PARAMETERS,
                                                      GROUP_MEMBER_ROLE_MODIFICATION_REQUEST_FIELDS
                                              ))
                                              .log().all()
                                              .patch(path);

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.NO_CONTENT.value());

        // TODO: id로 조회하여 검증
    }

    @Test
    void delete() {
        // given
        testEntityManager.persist(testUser);
        String path = "/groups/{groupId}/members/{memberId}";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath("/api")
                                                       .contentType(ContentType.JSON)
                                                       .header("Accept-Language", "en-US")
                                                       .header("Authorization", TEST_TOKEN)
                                                       .pathParam("groupId", 1L)
                                                       .pathParam("memberId", 1L);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      GROUP_MEMBER_DELETION_PATH_PARAMETERS
                                              ))
                                              .log().all()
                                              .delete(path);

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.NO_CONTENT.value());

        // TODO: id로 조회하여 검증
    }
}
