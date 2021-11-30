package com.postsquad.scoup.web.group.controller.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(staticName = "from")
@Builder
@Data
public class GroupReadAllResponses {

    private List<GroupReadAllResponse> groupReadAllResponse;
}
