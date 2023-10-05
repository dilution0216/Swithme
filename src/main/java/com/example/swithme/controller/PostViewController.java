package com.example.swithme.controller;

import com.example.swithme.dto.PostResponseDto;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.CommentService;
import com.example.swithme.service.PostService;
import com.example.swithme.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view")
public class PostViewController {

    private final PostService postService;

    // 전체 게시물 페이지
    @GetMapping("/posts")
    public String postsPage(){
        return "post/posts";
    }

    // 게시물 작성 페이지 - 작성자 닉네임 가져오기
    @GetMapping("/post/write")
    public String writePage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("info_username", userDetails.getUser().getNickname());
        return "post/postWrite";
    }

    // 게시물 상세 조회 - 게시물, 댓글, 대댓글 조회 / 게시물 삭제
    @GetMapping("/post/detail/{id}")
    public String postDetailPage(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto post = postService.lookupPost(id);

        // 게시물 작성자의 닉네임 가져오기
        String postAuthorNickname = postService.lookupPostUserNickname(id);
        // 현재 사용자의 닉네임
        String currentNickname = userDetails.getUser().getNickname();
        // 현재 사용자와 게시물 작성자 닉네임 비교
        boolean isCurrentUserAuthor = userDetails != null && userDetails.getUser().getNickname().equals(postAuthorNickname);
        model.addAttribute("post", post);
        model.addAttribute("currentNickname", currentNickname);
        model.addAttribute("isCurrentUserAuthor", isCurrentUserAuthor); // 컨트롤러에서 클라이언트로 전달
        return "post/postDetail";
    }

    // 게시글 수정 페이지
    @GetMapping("/post/update/{id}")
    public String modifyPost(Model model,  @PathVariable Long id)
            throws JsonProcessingException {
        model.addAttribute("post",postService.lookupPost(id));
        return "post/postUpdate";
    }
}
