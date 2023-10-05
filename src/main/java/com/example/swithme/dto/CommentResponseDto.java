package com.example.swithme.dto;

import com.example.swithme.entity.Comment;
import com.example.swithme.entity.Reply;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
public class CommentResponseDto {

    private Long id;
    private String content;
    private String userNickname;
    private String username;
    private String createdAt;
    private String modifiedAt;
    private List<ReplyResponseDto> replyResponseDtoList;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.userNickname = comment.getUser().getNickname();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAtFormatted();
        this.modifiedAt = comment.getModifiedAtFormatted();
        if (comment.getReplyList().size() > 0) {
            this.replyResponseDtoList = new ArrayList<>();
            for (Reply reply : comment.getReplyList()) {
                this.replyResponseDtoList.add(new ReplyResponseDto(reply));
            }
        }// getReplyList if() 끝
    }
}
