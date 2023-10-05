package com.example.swithme.entity.chat;

import com.example.swithme.dto.chat.ChatRoomRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatRoom {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String title;

    private String content;

    private UUID chatUrl; // 채팅방 입장 주소

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ChatGroup> chatGroups = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public ChatRoom(String title, String content, UUID chatUrl) {
        this.title = title;
        this.content = content;
        this.chatUrl = chatUrl;
    }

    public void update(ChatRoomRequestDto chatRoomRequestDto) {
        this.title = chatRoomRequestDto.getTitle();
        this.content = chatRoomRequestDto.getContent();
    }
}
