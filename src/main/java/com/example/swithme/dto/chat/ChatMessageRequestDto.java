package com.example.swithme.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatMessageRequestDto {

    private String sender;
    private String content;
}
