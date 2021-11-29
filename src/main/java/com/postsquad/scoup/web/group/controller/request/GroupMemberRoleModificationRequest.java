package com.postsquad.scoup.web.group.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GroupMemberRoleModificationRequest {

    // TODO: enum으로 변경필요
    private String role;
}
