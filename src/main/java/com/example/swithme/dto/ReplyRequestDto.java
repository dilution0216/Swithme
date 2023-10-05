package com.example.swithme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyRequestDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
