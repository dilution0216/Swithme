package com.example.swithme.dto.chat;

import com.example.swithme.entity.chat.ChatGroup;
import com.example.swithme.enumType.Invite;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatUserResponseDto {
    private Long id;
    private String username;
    private Invite invite;


    public ChatUserResponseDto(ChatGroup chatGroup) {
        this.id = chatGroup.getId();
        this.username = chatGroup.getUser().getUsername();
        this.invite = chatGroup.getInvite();
    }
}
