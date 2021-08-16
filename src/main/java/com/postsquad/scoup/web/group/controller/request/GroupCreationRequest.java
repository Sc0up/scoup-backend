package com.postsquad.scoup.web.group.controller.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class GroupCreationRequest extends GroupBaseRequest {

    @Builder
    public GroupCreationRequest(@NotEmpty String name, String description) {
        super(name, description);
    }
}
