package com.example.swithme.repository;

import com.example.swithme.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();
    List<Post> findAllByCategory_IdOrderByModifiedAtDesc(Long category_id);
}