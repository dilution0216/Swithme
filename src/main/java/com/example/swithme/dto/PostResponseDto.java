package com.example.swithme.dto;

import com.example.swithme.entity.Comment;
import com.example.swithme.entity.Post;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String createdAt;
    private String modifiedAt;
    private Long category_id;
    private Long user_id;
    private String username;
    private String userNickname;
    private String category_name;
    private String image;
    private List<CommentResponseDto> commentResponseDtoList;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.image = post.getPostImg();
        this.createdAt = post.getCreatedAtFormatted();
        this.modifiedAt = post.getModifiedAtFormatted();
        this.category_id = post.getCategory().getId();
        this.category_name = post.getCategory().getName();
        this.user_id = post.getUser().getUserId(); // post ->  User -> userId
        this.username = post.getUser().getUsername(); // post ->  User -> username
        this.userNickname = post.getUser().getNickname();
        if (post.getCommentList().size() > 0) {
            this.commentResponseDtoList = new ArrayList<>();
            for (Comment comment : post.getCommentList()) {
                this.commentResponseDtoList.add(new CommentResponseDto(comment));
            }
        }// getCommentList if() 끝
    }// end of constructor method()
}
