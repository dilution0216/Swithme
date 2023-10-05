package com.example.swithme.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="accumulatedtime")
public class AccumulatedTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 데이터베이스에서 값을 가져올 때 accumulatedMinutes가 null이 될 수 있음
    // 따라서 데이터베이스에 저장되는 값을 확인하고, 필요하다면 accumulatedMinutes 칼럼에 NOT NULL 제약 조건을 추가

    // accumulatedMinutes 칼럼에 NOT NULL 제약 조건을 추가하려면,
    // @Column 어노테이션에 nullable 속성을 false로 설정하면 된다.
    @Column(name = "accumulated_time")
    private Long accumulatedMinutes;

    // User : AccumulatedTime 간에 1:1 관계 설정 방법 1
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // 이 필드를 통해 마지막으로 업데이트 된 날짜를 추적
    // 매일 첫번째로 시간을 기록 할 때 오늘의 누적시간을 재설정 할 수 있음
    @Column(name = "last_updated_date")
    private LocalDate lastUpdatedDate;

    public AccumulatedTime() {
        this.accumulatedMinutes = 0L;
    }

    @Column(name = "today_accumulated_time")
    private Long todayAccumulatedMinutes = 0L;
}
