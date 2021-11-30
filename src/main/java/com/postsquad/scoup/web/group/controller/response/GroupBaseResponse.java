package com.postsquad.scoup.web.group.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupBaseResponse {

    private Long id;

    private String name;

    private String description;
}
