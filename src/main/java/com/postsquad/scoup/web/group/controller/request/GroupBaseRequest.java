package com.postsquad.scoup.web.group.controller.request;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class GroupBaseRequest {

    @NotEmpty
    private String name;

    private String description;
}
