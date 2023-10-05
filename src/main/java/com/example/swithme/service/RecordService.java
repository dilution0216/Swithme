package com.example.swithme.service;


import com.example.swithme.entity.AccumulatedTime;
import com.example.swithme.exception.RecordTimeException;
import com.example.swithme.repository.AccumulatedTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

public interface RecordService {
    void recordTime(String recordedTime, UserDetails userDetails);
    Long getTodayAccumulatedTime(UserDetails userDetails);
    Long getAccumulatedTime(UserDetails userDetails);
}