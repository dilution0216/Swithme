package com.example.swithme.controller;

import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.dto.ReplyRequestDto;
import com.example.swithme.dto.ReplyResponseDto;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    // 대댓글 생성
    @PostMapping("/post/comment/{id}/reply")  // id는 comment id
    @ResponseBody
    public ResponseEntity<ApiResponseDto> createReply (
            @PathVariable Long id,
            @RequestBody ReplyRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDto responseDto = replyService.createReply(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    // 대댓글 조회
    @GetMapping("/post/comment/reply/{id}") // id는 comment id
    @ResponseBody
    public List<ReplyResponseDto> replyList(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return replyService.replyList(id);
    }

    // 대댓글 수정
    @PutMapping("/post/comment/reply/{id}") // id는 reply id
    @ResponseBody
    public ResponseEntity<ApiResponseDto> updateReply(
            @PathVariable Long id,
            @RequestBody ReplyRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDto responseDto = replyService.updateReply(id,requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    // 대댓글 삭제
    @DeleteMapping("/post/comment/reply/{id}") // id는 reply id
    @ResponseBody
    public ResponseEntity<ApiResponseDto> deleteReply(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDto responseDto = replyService.deleteReply(id, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }
}
