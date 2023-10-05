package com.example.swithme.service;

import com.example.swithme.entity.AccumulatedTime;
import com.example.swithme.entity.User;
import com.example.swithme.exception.RecordTimeException;
import com.example.swithme.repository.AccumulatedTimeRepository;
import com.example.swithme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j // 이것은 Lombok 라이브러리의 일부로, @Slf4j 애노테이션은 클래스에 log 자동으로 추가합니다.
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final AccumulatedTimeRepository accumulatedTimeRepository;
    private final UserRepository userRepository;

    @Override
    public void recordTime(String recordedTime, UserDetails userDetails) {
        try {
            // 현재 로그인된 사용자의 정보를 가져오는 부분
            String username = userDetails.getUsername();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            // 해당 사용자의 누적 시간을 조회하거나 새로 생성
            AccumulatedTime accumulatedTime = accumulatedTimeRepository.findByUser(currentUser)
                    .orElseGet(() -> {
                        AccumulatedTime newAccumulatedTime = new AccumulatedTime();
                        newAccumulatedTime.setUser(currentUser);
                        currentUser.setAccumulatedTime(newAccumulatedTime);
                        return newAccumulatedTime;
                    });

            long newAccumulatedMinutes = accumulatedTime.getAccumulatedMinutes() +
                    parseRecordedTime(recordedTime);
            accumulatedTime.setAccumulatedMinutes(newAccumulatedMinutes);

            accumulatedTimeRepository.save(accumulatedTime);

            // 오늘 누적시간 업데이트
            LocalDate today = LocalDate.now();
            if(accumulatedTime.getLastUpdatedDate() == null || !today.isEqual(accumulatedTime.getLastUpdatedDate())) {
                accumulatedTime.setTodayAccumulatedMinutes(0L);  // 날짜가 변경되면 오늘 누적시간 초기화
                accumulatedTime.setLastUpdatedDate(today);
            }

            long newTodayAccumulatedMinutes = accumulatedTime.getTodayAccumulatedMinutes() +
                    parseRecordedTime(recordedTime);
            accumulatedTime.setTodayAccumulatedMinutes(newTodayAccumulatedMinutes);

            accumulatedTimeRepository.save(accumulatedTime);
        } catch (Exception e) {
            log.error("Error recording time", e);
            throw new RecordTimeException("시간을 기록하는 중에 오류가 발생했습니다.");
        }
    }

    // 문자열 형식의 시간을 분 단위로 파싱하는 메서드 (이 부분은 필요에 따라 수정)
    // parseRecordedTime 메소드에서 recordedTime 문자열이 올바르지 않은 형식으로 제공된 경우 NumberFormatException이 발생할 수 있다.
    private long parseRecordedTime(String recordedTime) {
        // 예: "01:30" 형식의 문자열을 파싱하여 분 단위로 변환
        String[] parts = recordedTime.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60 + seconds;
    }

    public Long getTodayAccumulatedTime(UserDetails userDetails) {
        String username = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        AccumulatedTime accumulatedTime = accumulatedTimeRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("사용자의 누적 시간을 찾을 수 없습니다."));

        LocalDate today = LocalDate.now();
        if(accumulatedTime.getLastUpdatedDate() == null || !today.isEqual(accumulatedTime.getLastUpdatedDate())) {
            accumulatedTime.setTodayAccumulatedMinutes(0L);  // 날짜가 변경되면 오늘 누적시간 초기화
            accumulatedTime.setLastUpdatedDate(today);
            accumulatedTimeRepository.save(accumulatedTime); // 변경사항 저장
        }

        return accumulatedTime.getTodayAccumulatedMinutes();
    }

    public Long getAccumulatedTime(UserDetails userDetails) {
        String username = userDetails.getUsername();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        AccumulatedTime accumulatedTime = accumulatedTimeRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("사용자의 누적 시간을 찾을 수 없습니다."));

        return accumulatedTime.getAccumulatedMinutes();
    }

}

