package com.postsquad.scoup.web.group.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(staticName = "from")
@Data
@Builder
public class GroupMemberReadAllResponses {

    private List<GroupMemberReadAllResponse> groupMembers;
}
