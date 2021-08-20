package com.postsquad.scoup.web.group.controller.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupBaseRequest {

    @NotEmpty
    private String name;

    private String description;
}
