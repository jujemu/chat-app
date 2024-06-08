package com.chatapp.kakaka.domain.friend.event.service;

import com.chatapp.kakaka.domain.friend.event.FriendRequestEvent;
import com.chatapp.kakaka.domain.friend.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public Long create(String senderName, String receiverName, String type) {
        FriendRequestEvent event = new FriendRequestEvent(senderName, receiverName, type);
        return eventRepository.save(event).getId();
    }
}
