package com.postsquad.scoup.web.user.repository;

import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("select case when count(u)>0 then true else false end from User u where :oAuthUser member of u.oAuthUsers")
    boolean existsByOAuthUser(@Param("oAuthUser") OAuthUser oAuthUser);
}
