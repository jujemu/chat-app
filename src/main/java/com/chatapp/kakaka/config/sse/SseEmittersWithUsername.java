package com.chatapp.kakaka.config.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
@AllArgsConstructor
public class SseEmittersWithUsername {

    private String username;
    private SseEmitter emitter;
}
