package com.postsquad.scoup.web.group.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GroupValidationResponse {

    private boolean isExistingName;

    @JsonProperty("is_existing_name")
    public boolean isExistingName() {
        return isExistingName;
    }
}
