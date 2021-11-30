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
        return new GroupReadAllResponse(group.getId(), group.getName(), group.getDescription());
    }
}
