package com.chatapp.kakaka.domain.user.controller;

import com.chatapp.kakaka.config.security.SecurityConfig;
import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class)}
)
class UserControllerTest {

    @Autowired
    UserController userController;

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("/create 로 유저를 생성할 수 있다.")
    @Test
    void registerUser() throws Exception {
        // given
        String username = "test";
        String password = "test password";
        RegisterUserRequest request = new RegisterUserRequest(username, password);

        // when
        ResultActions perform = mockMvc.perform(
                post("/create")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        // then
        verify(userService).registerUser(username, password);
        perform
                .andExpect(status().isOk());
    }

    @DisplayName("비어있는 이름으로 유저를 생성할 수 없다.")
    @Test
    void cannot_register_new_user_when_blank_username() throws Exception {
        // given
        String username = "";
        RegisterUserRequest request = getRegisterUserRequest(username);

        // when
        ResultActions perform = mockMvc.perform(
                post("/create")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid parameter included"));
    }

    private User createUser(String username) {
        return User.createUser(username, "test uuid");
    }

    private RegisterUserRequest getRegisterUserRequest(String username) {
        String password = "test password";
        return new RegisterUserRequest(username, password);
    }
}