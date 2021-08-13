package com.postsquad.scoup.web.user.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailValidationResponse {

    private boolean existingEmail;

    public static EmailValidationResponse valueOf(boolean existingEmail) {
        return new EmailValidationResponse(existingEmail);
    }
}
