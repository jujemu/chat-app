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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Base64;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockBean
    UserDetailsService userDetailsService;

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

    @DisplayName("등록된 아이디와 비밀번호로 로그인을 할 수 있다.")
    @Test
    void login() throws Exception {
        // given
        String username = "test";
        String password = "test password";
        User user = User.createUser(username, password);
        given(userDetailsService.loadUserByUsername(any())).willReturn(user.getUserDetails());

        String authorization = "Basic " +
                Base64.getEncoder().encodeToString(
                        (username + ":" + password).getBytes()
                );

        // when
        ResultActions perform = mockMvc.perform(
                get("/login")
                        .header("Authorization", authorization)
        );

        // then
        perform
                .andExpect(status().isOk());
    }

    @DisplayName("중복된 아이디로 회원가입할 수 없다. 또는 비밀번호가 틀리면 로그인할 수 없다.")
    @Test
    void login_with_invalid_password() throws Exception {
        // given
        String username = "test";
        String password = "test password";
        User originalUser = User.createUser(username, "original password");
        given(userDetailsService.loadUserByUsername(any())).willReturn(originalUser.getUserDetails());

        String authorization = "Basic " +
                Base64.getEncoder().encodeToString(
                        (username + ":" + password).getBytes()
                );

        // when
        ResultActions perform = mockMvc.perform(
                get("/login")
                        .header("Authorization", authorization)
        );

        // then
        perform
                .andExpect(status().isUnauthorized());
    }

    private RegisterUserRequest getRegisterUserRequest(String username) {
        String password = "test password";
        return new RegisterUserRequest(username, password);
    }
}