package com.stefano.nextbid.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signupWithEmptyBodyShouldFail() throws Exception {
        String body = "{}";
        this.mockMvc.perform(post("/api/auth/signup").content(body).contentType("application/json")).andExpect(status().isBadRequest());
    }

    @Test
    void signupWithMissingDataShouldFail() throws Exception {
        String body = "{\"name\":\"stefano\"}";
        this.mockMvc.perform(post("/api/auth/signup").content(body).contentType("application/json")).andExpect(status().isBadRequest());
    }

    @Test
    void signupWithValidDataShouldSuccess() throws Exception {
        String body = "{" +
                "\"name\":\"stefano\"," +
                "\"surname\":\"stefano\"," +
                "\"username\":\"stefano\"," +
                "\"password\":\"stefano\"," +
                "}";

        this.mockMvc.perform(post("/api/auth/signup").content(body).contentType("application/json")).andExpect(status().isOk());
    }
}