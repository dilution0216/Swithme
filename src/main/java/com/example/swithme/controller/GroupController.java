package com.example.swithme.controller;

import com.example.swithme.dto.*;
import com.example.swithme.dto.group.BoardRequestDto;
import com.example.swithme.dto.group.BoardResponseDto;
import com.example.swithme.dto.group.BoardUserResponseDto;
import com.example.swithme.entity.User;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    // 그룹보더 전제 조회
    @GetMapping("/groups")
    public ResponseEntity<List<BoardResponseDto>> getGroups() {
        List<BoardResponseDto> boardResponseDto = groupService.getGroups();
        return ResponseEntity.ok().body(boardResponseDto);
    }

    // 그룹보드 상세조회
    @GetMapping("/group/{groupId}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long groupId) {
        BoardResponseDto boardResponseDto = groupService.getBoard(groupId);
        return ResponseEntity.ok().body(boardResponseDto);
    }
    
    // 그룹 보더 생성
    @PostMapping("/group")
    @ResponseBody
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto boardResponseDto = groupService.createBoard(boardRequestDto, userDetails.getUser());

        return ResponseEntity.ok().body(boardResponseDto);
    }

    // 그룹보더 수정
    @PutMapping("/group/{groupId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long groupId,
                                                        @RequestBody BoardRequestDto boardRequestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BoardResponseDto boardResponseDto = groupService.updateBoard(groupId, boardRequestDto, userDetails.getUser());

        return ResponseEntity.ok().body(boardResponseDto);
    }

    // 그룹보더 삭제
    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<ApiResponseDto> deleteBoard(@PathVariable Long groupId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            groupService.deleteBoard(groupId, userDetails.getUser());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok().body(new ApiResponseDto("보드를 삭제하였습니다.", HttpStatus.OK.value()));
    }

    // 특정 그룹보더 회원 조회
    @GetMapping("/group/{groupId}/user")
    public ResponseEntity<BoardUserResponseDto> getBoardUser(@PathVariable Long groupId) {
        BoardUserResponseDto boardUserResponseDto = groupService.getBoardUser(groupId);
        return ResponseEntity.ok().body(boardUserResponseDto);
    }

    // 그룹보더 초대
    @PostMapping("/group/{groupId}/invite/{userId}")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> inviteBoard(@PathVariable Long groupId,
                                                      @PathVariable Long userId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            groupService.inviteBoard(groupId, userId, userDetails.getUser());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok().body(new ApiResponseDto("유저를 초대하였습니다.", HttpStatus.OK.value()));
    }
}
