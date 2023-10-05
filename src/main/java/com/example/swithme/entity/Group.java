package com.example.swithme.entity;


import com.example.swithme.dto.group.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "groupss") // group, groups 예약어 이슈
public class Group {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // 보드 번호

  @Column(nullable = false)
  private String groupName; // 보드 이름

  @Column(nullable = true)
  private String description; // 보드 설명

  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GroupUser> groupUsers = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private User creator; // 보드 생성자


  public Group(BoardRequestDto boardRequestDto, User user) {
    this.groupName = boardRequestDto.getGroupName();
    this.description = boardRequestDto.getDescription();
    this.creator = user;
  }

  public void addGroupUsers(GroupUser groupUser) {
    this.groupUsers.add(groupUser);
  }

  public void update(BoardRequestDto boardRequestDto) {
    this.groupName = boardRequestDto.getGroupName();
    this.description = boardRequestDto.getDescription();
  }
}