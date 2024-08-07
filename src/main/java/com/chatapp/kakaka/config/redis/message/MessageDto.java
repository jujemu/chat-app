package com.chatapp.kakaka.config.redis.message;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class MessageDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // TODO MessageDto 필드를 줄이거나 String 으로 전송해도 괜찮을거 같다.
    private String type;
    private String sender;
    private String receiver;

    @Builder
    public MessageDto(String type, String sender, String receiver) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
    }
}
