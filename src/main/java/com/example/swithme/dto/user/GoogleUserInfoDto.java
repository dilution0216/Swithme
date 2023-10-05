package com.example.swithme.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleUserInfoDto {
    private String id;
    private String nickname;
    private String email;

    public GoogleUserInfoDto(String id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}