package com.example.swithme.entity;

import com.example.swithme.dto.user.UserUpdateRequestDto;
import com.example.swithme.enumType.UserRole;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username; //유저네임 = 이메일로 받음

    @Column(name = "password")
    private String password;

    @Column(name = "nickname", unique = true)
    private String nickname;

    private int status;

    @Enumerated(value = EnumType.STRING)
    private UserRole role = UserRole.USER;

    private Long kakaoId;

    private String googleId;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<GroupUser> groupUsers;

    // // User : AccumulatedTime 간에 1:1 관계 설정 방법 2 이걸로
    // 이게 더 자연스러움
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AccumulatedTime accumulatedTime;

    // 일반 회원가입

    public User(String username, String password, String nickname, int status) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
    }

    // 카카오 로그인시 회원가입
    public User(String username, String password, String nickname, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
    }

    // 구글 로그인시 회원가입
    public User(String username, String password, String nickname, String googleId) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.googleId = googleId;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public User googleIdUpdate(String googleId) {
        this.googleId = googleId;
        return this;
    }

    public void update(UserUpdateRequestDto userUpdateRequestDto) {
        this.nickname = userUpdateRequestDto.getNickname();
    }

}
