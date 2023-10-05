package com.example.swithme.repository;

import com.example.swithme.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository <Comment, Long> {
    List<Comment> findAllByPostIdOrderByCreatedAt(Long id);
}
