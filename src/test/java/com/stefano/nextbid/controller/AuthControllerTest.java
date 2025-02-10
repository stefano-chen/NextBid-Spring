package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.SigninBody;
import com.stefano.nextbid.dto.SignupBody;
import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;


    @Test
    void signupWithEmptyBodyShouldFail() throws Exception {
        String body = "{}";
        this.mockMvc.perform(post("/api/auth/signup").content(body).contentType("application/json")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void signupWithMissingDataShouldFail() throws Exception {
        String body = "{\"name\":\"stefano\"}";
        this.mockMvc.perform(post("/api/auth/signup").content(body).contentType("application/json")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void signupWithValidDataShouldSuccess() throws Exception {
        String body = "{" +
                "\"name\":\"stefano\"," +
                "\"surname\":\"stefano\"," +
                "\"username\":\"stefano\"," +
                "\"password\":\"stefano\"" +
                "}";

        SignupBody signupBody = new SignupBody("stefano", "stefano", "stefano", "stefano");

        when(authService.signup(signupBody)).thenReturn(new UserDTO(1, "stefano", "stefano", "stefano", "", Instant.now()));

        this.mockMvc.perform(post("/api/auth/signup").content(body).contentType("application/json")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("stefano"))
                .andExpect(jsonPath("$.surname").value("stefano"))
                .andExpect(jsonPath("$.username").value("stefano"))
                .andExpect(jsonPath("$.password").doesNotExist());
        verify(authService, times(1)).signup(signupBody);
    }

    @Test
    void signinWithEmptyBodyShouldFail() throws Exception {
        String body = "{}";
        this.mockMvc.perform(post("/api/auth/signin").content(body).contentType("application/json")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void signinWithMissingDataShouldFail() throws Exception {
        String body = "{\"name\":\"stefano\"}";
        this.mockMvc.perform(post("/api/auth/signup").content(body).contentType("application/json")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void signinWithValidDataShouldSuccess() throws Exception {
        String body = "{" +
                "\"username\":\"stefano\"," +
                "\"password\":\"stefano\"" +
                "}";

        SigninBody signinBody = new SigninBody("stefano", "stefano");

        when(authService.signin(signinBody)).thenReturn(new UserDTO(1, "stefano", "stefano", "stefano", "", Instant.now()));

        this.mockMvc.perform(post("/api/auth/signin").content(body).contentType("application/json")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("stefano"))
                .andExpect(jsonPath("$.surname").value("stefano"))
                .andExpect(jsonPath("$.username").value("stefano"))
                .andExpect(jsonPath("$.password").doesNotExist());
        ;
        verify(authService, times(1)).signin(signinBody);
    }
}