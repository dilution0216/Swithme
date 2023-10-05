package com.example.swithme.repository;

import com.example.swithme.entity.User;
import com.example.swithme.enumType.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByKakaoId(Long kakaoId);
    Optional<User> findByGoogleId(String googleId);

    List<User> findAllByRole(UserRole role);

    Optional<User> findByNickname(String username);

}