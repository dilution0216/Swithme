package com.example.swithme.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 날짜형태 지정

    @CreatedDate
    @Column(updatable = false)
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;

    public String getCreatedAtFormatted() {
        return createdAt.format(FORMATTER);
    }

    public String getModifiedAtFormatted() {
        return modifiedAt.format(FORMATTER);
    }

}