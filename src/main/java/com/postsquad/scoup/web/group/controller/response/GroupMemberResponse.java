package com.postsquad.scoup.web.group.controller.response;

import com.postsquad.scoup.web.image.controller.ImageResponse;
import lombok.Getter;

@Getter
public class GroupMemberResponse {

    private Long userId;

    private String nickname;

    private String email;

    private ImageResponse avatarUrl;
}
