package com.postsquad.scoup.web.user.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailValidationResponse {

    private Boolean isExistingEmail = false;

    public static EmailValidationResponse valueOf(boolean isExistingEmail) {
        return new EmailValidationResponse(isExistingEmail);
    }
}
