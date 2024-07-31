package com.chatapp.kakaka.config.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String message;
    private String sender;
    private String roomId; // 메세지 방 번호 || 타겟 Channel
}
