package com.example.swithme.service;

import com.example.swithme.dto.group.BoardRequestDto;
import com.example.swithme.dto.group.BoardResponseDto;
import com.example.swithme.dto.group.BoardUserResponseDto;
import com.example.swithme.entity.Group;
import com.example.swithme.entity.GroupUser;
import com.example.swithme.entity.User;
import com.example.swithme.repository.GroupRepository;
import com.example.swithme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    // 그룹보더 전체조회
    public List<BoardResponseDto> getGroups() {
        List<Group> groups = groupRepository.findAll();
        List<BoardResponseDto> boardResponseDto = new ArrayList<>();

        for (Group group : groups) {
            boardResponseDto.add(new BoardResponseDto(group));
        }

        return boardResponseDto;
    }

    // 그룹보더 상세조회
    public BoardResponseDto getBoard(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("조회할 보드가 존재하지 않습니다.")
        );

        return new BoardResponseDto(group);
    }


    // 그룹보더 생성
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, User user) {
        if (user == null) {
            throw new IllegalArgumentException("로그인 후 시도해주세요.");
        }

        Group group = new Group(boardRequestDto, user); // 보드 생성
        GroupUser groupUser = new GroupUser(user, group);

        group.addGroupUsers(groupUser); // 보드사용자 목록에 유저추가

        groupRepository.save(group);

        return new BoardResponseDto(group);
    }

    // 그룹보더 수정
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto boardRequestDto, User user) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("수정할 보드가 존재하지 않습니다.")
        );

        if (group.getCreator().getUserId().equals(user.getUserId())) {
            group.update(boardRequestDto);
        } else {
            throw new IllegalArgumentException("보드 생성자만 수정이 가능합니다.");
        }

        return new BoardResponseDto(group);
    }

    // 그룹보더 삭제
    @Transactional
    public void deleteBoard(Long id, User user) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("삭제할 보드가 존재하지 않습니다.")
        );

        if (group.getCreator().getUserId().equals(user.getUserId())) {
            groupRepository.delete(group);
        } else {
            throw new IllegalArgumentException("보드 생성자만 삭제가 가능합니다.");
        }
    }

    // 보드 초대
    @Transactional
    public void inviteBoard(Long groupId, Long userid, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new IllegalArgumentException("초대할 보드가 존재하지 않습니다.")
        );

        if(!group.getCreator().getUserId().equals(user.getUserId())){
            throw new IllegalArgumentException("보드 생성자만 초대할 수 있습니다.");
        }

        User invitedUser = userRepository.findById(userid).orElseThrow(
                () -> new IllegalArgumentException("초대받을 유저가 존재하지 않습니다.")
        );

        if (group.getGroupUsers().stream()
                .anyMatch(boardUser -> boardUser.getUser().equals(invitedUser))) {
            throw new IllegalArgumentException("이미 보드에 포함된 유저입니다.");
        } else {
            GroupUser groupUser = new GroupUser(invitedUser, group);
            group.addGroupUsers(groupUser);
            groupRepository.save(group);
        }
    }

    public BoardUserResponseDto getBoardUser(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new IllegalArgumentException("조회할 보드가 존재하지 않습니다.")
        );
        return new BoardUserResponseDto(group);
    }
}
