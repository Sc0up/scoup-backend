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

    @Query(value = "select count(*) from \"oauth_user\" where \"oauth_type\"=:oAuthType and \"social_service_id\"=:socialServiceId",
           nativeQuery = true)
    long existsByOAuthUser(@Param("oAuthType") String oAuthType, @Param("socialServiceId") String socialServiceId);

    default boolean existsByOAuthUser(OAuthUser oAuthUser) {
        return existsByOAuthUser(oAuthUser.getOAuthTypeName(), oAuthUser.getSocialServiceId()) > 0;
    }
}
