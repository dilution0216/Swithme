package com.example.swithme.dto.group;

import com.example.swithme.entity.Group;
import com.example.swithme.entity.GroupUser;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardResponseDto {
    private Long id; // 보드 번호
    private String groupName; // 보드 이름
    private String description; // 보드 설명

    public BoardResponseDto(Group group){
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.description = group.getDescription();

    }
}