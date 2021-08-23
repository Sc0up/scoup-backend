package com.postsquad.scoup.web.auth.controller.response;

import lombok.Getter;

@Getter
public class KakaoUserResponse {

    private Long id;

    private KakaoAccount kakaoAccount;

    @Getter
    static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        static class Profile {
            private String profileImageUrl;
            private String nickname;
        }
    }
}
