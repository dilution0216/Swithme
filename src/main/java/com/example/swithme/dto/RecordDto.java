package com.example.swithme.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordDto {
    private String recordedTime;

    public void setRecordedTime(String recordedTime) {
        this.recordedTime = recordedTime;
    }
}
