package com.example.swithme.entity;

import com.example.swithme.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();

    public Comment(CommentRequestDto requestDto, Post post, User user) {
        this.content = requestDto.getContent();
        this.user = user;
        this.post = post;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
