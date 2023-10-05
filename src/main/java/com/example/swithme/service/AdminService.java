package com.example.swithme.service;

import com.example.swithme.entity.Post;
import com.example.swithme.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    PostRepository postRepository;


    public List<Post> findAll() {
        List<Post> findAll = postRepository.findAll();
        return findAll;
    }
}

