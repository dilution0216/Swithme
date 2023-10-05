package com.example.swithme.dto.group;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDetailsRequestDto {
    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String GroupGoals;
}
