package com.postsquad.scoup.web.group.repository;

import com.postsquad.scoup.web.group.domain.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, Long> {

    boolean existsByName(String name);
}
