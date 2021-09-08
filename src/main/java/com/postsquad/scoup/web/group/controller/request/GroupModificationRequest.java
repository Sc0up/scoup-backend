package com.postsquad.scoup.web.group.controller.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupModificationRequest extends GroupBaseRequest {

    @Builder
    public GroupModificationRequest(@NotEmpty String name, String description) {
        super(name, description);
    }
}
