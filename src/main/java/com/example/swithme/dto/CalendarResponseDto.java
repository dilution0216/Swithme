package com.example.swithme.dto;

import com.example.swithme.entity.Calendar;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CalendarResponseDto {
    private Long id;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;

    public CalendarResponseDto(Calendar calendar) {
        this.id = calendar.getId();
        this.title = calendar.getName();
        this.start = calendar.getStartTime();
        this.end = calendar.getEndTime();
    }
}
