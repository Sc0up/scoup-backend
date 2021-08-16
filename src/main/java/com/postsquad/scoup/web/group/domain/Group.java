package com.postsquad.scoup.web.group.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "GROUP_TABLE", // TODO: GROUP is reserved word, 팀원들과 상의 후 테이블 이름 결정
       uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "UK_GROUP_NAME"),
})
@Entity
public class Group extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;

    protected Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Builder
    public static Group of(String name, String description) {
        return new Group(name, description);
    }
}
