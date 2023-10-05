package com.example.swithme.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String nickname;
    private String username;

    public KakaoUserInfoDto(Long id, String nickname, String username) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
    }
}