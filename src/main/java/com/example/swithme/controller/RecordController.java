package com.example.swithme.controller;

import com.example.swithme.dto.RecordDto;
import com.example.swithme.entity.AccumulatedTime;
import com.example.swithme.exception.RecordTimeException;
import com.example.swithme.repository.AccumulatedTimeRepository;
import com.example.swithme.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<String> recordTime(@RequestBody RecordDto recordDto,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            recordService.recordTime(recordDto.getRecordedTime(), userDetails);
            return ResponseEntity.ok("시간이 성공적으로 기록되었습니다.");
        } catch (RecordTimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("시간을 기록하는 중에 오류가 발생했습니다.");
        }
    }

    @GetMapping("/today")
    public ResponseEntity<Long> getTodayAccumulatedTime(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(recordService.getTodayAccumulatedTime(userDetails));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L);
        }
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getAccumulatedTime(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(recordService.getAccumulatedTime(userDetails));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L);
        }
    }
}


