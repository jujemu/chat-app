package com.chatapp.kakaka.config.sse;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Getter
@Component
public class SseEmitters {

    private final List<SseEmittersWithUsername> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter add(String username, SseEmitter emitter) {
        SseEmittersWithUsername sseEmittersWithUsername = new SseEmittersWithUsername(username, emitter);
        this.emitters.add(sseEmittersWithUsername);
        log.info("new emitter added: {} with name {}", emitter, username);
        log.info("emitter list size: {}", emitters.size());
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitters.remove(sseEmittersWithUsername);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }
}
