package com.example.swithme.service;

import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.dto.ReplyRequestDto;
import com.example.swithme.dto.ReplyResponseDto;
import com.example.swithme.entity.Comment;
import com.example.swithme.entity.Reply;
import com.example.swithme.entity.User;
import com.example.swithme.exception.CommentNotFoundException;
import com.example.swithme.repository.CommentRepository;
import com.example.swithme.repository.ReplyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    // 대댓글 생성
    public ApiResponseDto createReply(Long comment_id, ReplyRequestDto requestDto, User user) {
        // 댓글 유무 확인
        Comment comment = commentRepository.findById(comment_id).orElseThrow(
                () -> new CommentNotFoundException("댓글이 존재하지 않습니다."));

        // 대댓글에 저장하기
        Reply reply = new Reply(requestDto, user, comment);
        replyRepository.save(reply);
        return new ApiResponseDto("대댓글이 저장되었습니다.", HttpStatus.OK.value());
    }

    // 대댓글 조회
    public List<ReplyResponseDto> replyList(Long id) {
        List<Reply> getReplyList = replyRepository.findAllByCommentIdOrderByCreatedAt(id);
        List<ReplyResponseDto> newReplyList = getReplyList.stream().map(ReplyResponseDto::new).toList();
        return newReplyList;
    }

    // 대댓글 수정
    @Transactional
    public ApiResponseDto updateReply(Long comment_id, ReplyRequestDto requestDto, User user) {
        // 대댓글 유무 확인
        Reply reply = replyRepository.findById(comment_id).orElseThrow(
                () -> new CommentNotFoundException("댓글이 존재하지 않습니다."));
        // 작성자 확인
        Long writerId = reply.getUser().getUserId();
        Long loginId = user.getUserId();
        if(!writerId.equals(loginId)){
            throw new IllegalArgumentException("댓글의 작성자가 아니므로 수정이 불가합니다.");
        }

        // 댓글 수정
        reply.update(requestDto);
        return new ApiResponseDto ("댓글 수정이 완료되었습니다.", HttpStatus.OK.value());
    }

    // 대댓글 삭제
    public ApiResponseDto deleteReply(Long comment_id, User user) {
        // 대댓글 유무 확인
        Reply reply = replyRepository.findById(comment_id).orElseThrow(
                () -> new CommentNotFoundException("댓글이 존재하지 않습니다."));
        // 작성자 확인
        Long writerId = reply.getUser().getUserId();
        Long loginId = user.getUserId();
        if(!writerId.equals(loginId)){
            throw new IllegalArgumentException("댓글의 작성자가 아니므로 삭제가 불가합니다.");
        }

        // 댓글 삭제
        replyRepository.delete(reply);
        return new ApiResponseDto ("댓글 삭제가 완료되었습니다.", HttpStatus.OK.value());
    }
}
