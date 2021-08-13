package com.postsquad.scoup.web.user.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailValidationResponse {

    private boolean existingEmail;

    public static EmailValidationResponse valueOf(boolean existingEmail) {
        return new EmailValidationResponse(existingEmail);
    }
}
