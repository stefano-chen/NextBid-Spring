package com.stefano.nextbid.controller;

import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.repo.UserRepository;
import com.stefano.nextbid.service.SessionManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UtilsController.class)
class UtilsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private SessionManager sessionManager;
    @MockitoBean
    private UserRepository userRepository;

    @Test
    void hittingWhoamiWhileAuthenticatedShouldSuccess() throws Exception {
        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(sessionManager.getUserId()).thenReturn(1);
        User user = new User("stefanoss", "stefano", "chen", "bio", "hello");
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        this.mockMvc.perform(get("/api/whoami")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$._id").value(1))
                .andExpect(jsonPath("$.username").value("stefanoss"));
    }

    @Test
    void hittingWhoamiWhileUnauthenticatedShouldFail() throws Exception {
        when(sessionManager.isAuthenticated()).thenReturn(false);
        this.mockMvc.perform(get("/api/whoami")).andDo(print()).andExpect(status().isUnauthorized());
    }
}