package com.postsquad.scoup.web.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DefaultPostResponse {
    private long id;

    public static DefaultPostResponse from(BaseEntity entity) {
        return new DefaultPostResponse(entity.getId());
    }
}
