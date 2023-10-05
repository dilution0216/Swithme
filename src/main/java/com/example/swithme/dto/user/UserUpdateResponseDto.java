package com.example.swithme.dto.user;

import com.example.swithme.entity.AccumulatedTime;
import com.example.swithme.entity.User;
import lombok.Getter;

@Getter
public class UserUpdateResponseDto {
    private Long userId;
    private String username;
    private String nickname;
    private Long accumulatedTime;

    public UserUpdateResponseDto(User user, AccumulatedTime accumulatedTime) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        if(accumulatedTime != null) {
            this.accumulatedTime = accumulatedTime.getAccumulatedMinutes();
        } else {
            this.accumulatedTime = 0L;
        }
    }
}