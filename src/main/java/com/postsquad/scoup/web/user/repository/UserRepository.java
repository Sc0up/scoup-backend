package com.postsquad.scoup.web.user.repository;

import com.postsquad.scoup.web.user.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
