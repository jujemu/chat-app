package com.chatapp.kakaka.config.sse;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Getter
@Component
public class SseEmitters {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter add(String username, SseEmitter emitter) {
        this.emitters.put(username, emitter);
        log.info("new emitter added: {} with name {}", emitter, username);
        log.info("emitter list size: {}", emitters.size());
        log.info("emitter list: {}", emitters.keySet());

        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitters.remove(username);
            try {
                emitter.send(
                        SseEmitter.event()
                                .id("")
                                .name("Complete")
                                .data("Complete")
                );
            } catch (IOException e) {
                throw new IllegalArgumentException("SSE 연결을 알리는 과정에서 문제가 생겼습니다.");
            }
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });
        return emitter;
    }
}
