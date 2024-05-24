package com.chatapp.kakaka.domain.friend.controller.dto;

import com.chatapp.kakaka.domain.friend.Friend;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendListResponse {

    private String myName;
    private List<FriendResponse> friends;

    public static FriendListResponse of(String myName, List<Friend> friends) {
        List<FriendResponse> response = friends.stream()
                .map(friend ->
                        new FriendResponse(
                                friend.getId(),
                                friend.getSender().getUsername(),
                                friend.getType())
                ).toList();
        return new FriendListResponse(myName, response);
    }
}
