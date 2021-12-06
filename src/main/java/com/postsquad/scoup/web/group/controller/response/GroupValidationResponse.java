package com.postsquad.scoup.web.group.controller.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GroupValidationResponse {

    @Builder.Default
    private Boolean isExistingName = false;
}
