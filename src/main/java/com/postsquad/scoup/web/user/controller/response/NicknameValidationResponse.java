package com.postsquad.scoup.web.user.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NicknameValidationResponse {

    private boolean existingNickname;

    public static NicknameValidationResponse valueOf(boolean existingNickname) {
        return new NicknameValidationResponse(existingNickname);
    }
}
