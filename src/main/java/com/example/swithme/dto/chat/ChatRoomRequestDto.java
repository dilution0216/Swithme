package com.example.swithme.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatRoomRequestDto {
    private Long id;

    private String title;

    private String content;

    private UUID chatUrl;
}
