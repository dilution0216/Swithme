package com.example.swithme.repository;

import com.example.swithme.entity.AccumulatedTime;
import com.example.swithme.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccumulatedTimeRepository extends JpaRepository<AccumulatedTime,Long> {
    // 누적시간으로 내림차순 정렬하는 메서드 생성하기
    Optional<AccumulatedTime> findByUser(User user);

}