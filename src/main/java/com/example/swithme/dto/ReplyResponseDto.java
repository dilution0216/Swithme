package com.example.swithme.dto;

import com.example.swithme.entity.Reply;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyResponseDto {
    private Long id;
    private String content;
    private String userNickname;
    private String username;
    private String createdAt;
    private String modifiedAt;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.userNickname = reply.getUser().getNickname();
        this.createdAt = reply.getCreatedAtFormatted();
        this.username = reply.getUser().getUsername();
        this.modifiedAt = reply.getModifiedAtFormatted();
    }
}
