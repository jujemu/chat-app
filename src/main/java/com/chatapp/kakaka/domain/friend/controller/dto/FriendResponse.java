package com.chatapp.kakaka.domain.friend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FriendResponse {

    private Long id;
    private String yours;

}
