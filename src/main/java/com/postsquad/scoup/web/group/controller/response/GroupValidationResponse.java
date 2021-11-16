package com.postsquad.scoup.web.group.controller.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GroupValidationResponse {

    @Builder.Default
    private Boolean isExistingName = false;
}
