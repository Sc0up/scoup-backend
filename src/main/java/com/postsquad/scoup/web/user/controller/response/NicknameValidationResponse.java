package com.postsquad.scoup.web.user.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NicknameValidationResponse {

    private Boolean isExistingNickname = false;

    public static NicknameValidationResponse valueOf(boolean isExistingNickname) {
        return new NicknameValidationResponse(isExistingNickname);
    }
}
