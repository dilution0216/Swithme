package com.example.swithme.entity;

import com.example.swithme.dto.ReplyRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reply")
public class Reply extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    public Reply(ReplyRequestDto requestDto, User user, Comment comment) {
        this.content = requestDto.getContent();
        this.user = user;
        this.comment = comment;
    }

    public void update(ReplyRequestDto requestDto){
        this.content = requestDto.getContent();
    }
}
