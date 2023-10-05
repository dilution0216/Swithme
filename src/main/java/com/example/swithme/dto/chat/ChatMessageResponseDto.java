package com.example.swithme.dto.chat;

import com.example.swithme.entity.chat.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageResponseDto {

    private String sender;
    private String content;

    public ChatMessageResponseDto(ChatMessage chatMessage) {
        this.sender = chatMessage.getUser().getNickname();
        this.content = chatMessage.getContent();
    }
}
