package com.example.swithme.entity.chat;

import com.example.swithme.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "CHATROOM_ID")
    private ChatRoom chatRoom;

    public ChatMessage(String content, ChatRoom chatRoom, User user) {
        this.content = content;
        this.chatRoom = chatRoom;
        this.user = user;
    }
}
