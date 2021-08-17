package com.postsquad.scoup.web.group.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {
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
