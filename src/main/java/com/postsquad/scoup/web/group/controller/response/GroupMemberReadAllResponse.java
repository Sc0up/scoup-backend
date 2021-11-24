package com.postsquad.scoup.web.group.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GroupMemberReadAllResponse {

    private long userId;

    private String nickname;

    private String email;

    private String avatarUrl;
}
