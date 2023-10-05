package com.example.swithme.repository;

import com.example.swithme.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository <Reply, Long> {
    List<Reply> findAllByCommentIdOrderByCreatedAt(Long id);
}
