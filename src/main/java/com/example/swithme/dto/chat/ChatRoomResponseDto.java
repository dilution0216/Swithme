package com.example.swithme.dto.chat;

import com.example.swithme.entity.chat.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatRoomResponseDto {

    private Long id;

    private String title;

    private String content;

    private UUID chatUrl; // 들어가는 방 주소가 변경됨

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.title = chatRoom.getTitle();
        this.content = chatRoom.getContent();
        this.chatUrl = chatRoom.getChatUrl();
    }
}
