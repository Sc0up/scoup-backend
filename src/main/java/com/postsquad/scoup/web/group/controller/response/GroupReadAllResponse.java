package com.postsquad.scoup.web.group.controller.response;

import com.postsquad.scoup.web.group.domain.Group;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class GroupReadAllResponse extends GroupBaseResponse {

    @Builder
    protected GroupReadAllResponse(Long id, String name, String description) {
        super(id, name, description);
    }

    public static GroupReadAllResponse from(Group group) {
        return GroupReadAllResponse.builder().id(group.getId()).name(group.getName()).description(group.getDescription()).build();
    }
}
