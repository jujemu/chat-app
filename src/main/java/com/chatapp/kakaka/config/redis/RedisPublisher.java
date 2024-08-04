package com.chatapp.kakaka.config.redis;

import com.chatapp.kakaka.config.redis.message.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> template;

    /*
        Java object publish
     */
    public void publish(ChannelTopic topic, MessageDto dto) {
        template.convertAndSend(topic.getTopic(), dto);
    }
}
