package com.example.swithme.dto.user;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PasswordRequestDto {

    String password;

    @Pattern(regexp = "^[A-Za-z0-9]{4,15}$", message = "비밀번호는 4~15 글자의 영문 숫자 조합이어야 합니다.")
    String newPassword;

    @Pattern(regexp = "^[A-Za-z0-9]{4,15}$", message = "비밀번호는 4~15 글자의 영문 숫자 조합이어야 합니다.")
    String checkPassword;
}
