package com.postsquad.scoup.web.group.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
// TODO: 전체조회 머지되면 BaseResponse와 합치기
public class GroupReadOneResponse {
    
    private long id;

    private String name;

    private String description;

    private String image;
}
