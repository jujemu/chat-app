package com.chatapp.kakaka.domain.friend.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FriendRequestEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String senderName;
    private String receiverName;
    private String type;

    public FriendRequestEvent(String senderName, String receiverName, String type) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.type = type;
    }
}
