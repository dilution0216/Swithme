package com.example.swithme.entity;

import com.example.swithme.dto.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 50000)
    private String content;

    @Column
    private String postImg;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    // 게시글 작성
    public Post(PostRequestDto requestDto, User user, Category category, String postImg) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
        this.category = category;
        this.postImg = postImg;
    }

    public void update(PostRequestDto postRequestDto, Category category, String postImg) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.category = category;
        this.postImg = postImg;
        this.postImg = postImg;
    }

    //게시글 사진 수정시
    public void updateImage(String postImg) {
        this.postImg = postImg;
    }

    //게시글 사진 삭제 시
    public void deleteImage(String postImg) {
        this.postImg = null;
    }
}
