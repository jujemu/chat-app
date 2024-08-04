package com.chatapp.kakaka.config.redis.message;

import lombok.AllArgsConstructor;
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
    private Long id;
    private String message;
    private String sender;
    private String roomId;

    @Builder
    public MessageDto(Long id, String message, String sender, String roomId) {
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.roomId = roomId;
    }
}
