package com.example.swithme.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
public class SseController {

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> sse() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "SSE Event #" + sequence);
    }
}


