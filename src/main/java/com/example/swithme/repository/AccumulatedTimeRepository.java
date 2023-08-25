package com.example.swithme.repository;

import com.example.swithme.entity.AccumulatedTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccumulatedTimeRepository extends JpaRepository<AccumulatedTime,Long> {
    // 누적시간으로 내림차순 정렬하는 메서드 생성하기
}