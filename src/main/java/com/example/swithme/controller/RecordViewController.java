package com.example.swithme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecordViewController {

    @GetMapping("/stopwatch")
    public String showStopwatch() {
        return "stopwatch/stopwatch"; // stopwatch.html을 반환
    }
}

