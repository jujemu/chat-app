package com.chatapp.kakaka.domain.friend.controller;

import com.chatapp.kakaka.domain.friend.service.FriendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = FriendController.class
)
class FriendControllerTest {

    @Autowired
    FriendController friendController;

    @MockBean
    FriendService friendService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("내 친구 목록을 조회할 수 있다.")
    @WithMockUser
    @Test
    void showAll() throws Exception {
        // given
        String myName = "my";
        given(friendService.showAll(any())).willReturn(any());

        // when
        ResultActions perform = mockMvc.perform(
                get("/friend/all/" + myName)
        );

        // then
        verify(friendService).showAll(any());
        perform.andExpect(status().isOk());
    }

    @DisplayName("친구 요청을 보낼 수 있다.")
    @WithMockUser
    @Test
    void sendRequest() throws Exception {
        // given
        String myName = "my";
        String receiverName = "receiver";

        // when
        ResultActions perform = mockMvc.perform(
                post("/friend/request/" + myName + "/" + receiverName)
                        .with(csrf())
        );

        // then
        verify(friendService).sendRequest(any(), any());
        perform.andExpect(status().isOk());
    }
}