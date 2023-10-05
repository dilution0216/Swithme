package com.example.swithme.dto.user;
import com.example.swithme.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDto {
    private String username;
    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    @NotBlank(message = "이메일을 입력해주세요")

//    @Email
//    private String email;

    public UserUpdateRequestDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}