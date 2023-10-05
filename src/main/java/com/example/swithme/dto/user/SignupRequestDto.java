package com.example.swithme.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "이메일을 입력해주세요")
    @Email
    private String username; //아이디, 이메일

    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{4,15}$",
            message = "최소 4자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자 로 구성되어야 합니다.")
    private String password;

    //비밀번호 확인
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{4,15}$",
            message = "최소 4자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자 로 구성되어야 합니다.")
    private String checkPassword;

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickName;

}
