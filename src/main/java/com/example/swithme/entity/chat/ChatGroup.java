package com.example.swithme.entity.chat;

import com.example.swithme.entity.User;
import com.example.swithme.enumType.ChatRole;
import com.example.swithme.enumType.Invite;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Enumerated(value = EnumType.STRING)
    private ChatRole chatRole;

    @Enumerated(value = EnumType.STRING)
    private Invite invite;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "CHATROOM_ID")
    private ChatRoom chatRoom;

    public ChatGroup(User user, ChatRoom chatRoom, ChatRole chatRole) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.chatRole = chatRole;
    }

    public ChatGroup(User user, ChatRoom chatRoom, ChatRole chatRole, Invite invite) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.chatRole = chatRole;
        this.invite = invite;
    }
}
