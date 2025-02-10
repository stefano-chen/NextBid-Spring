package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void getUsersWhenThereAreNoUsersShouldReturnEmptyList() throws Exception {
        when(userService.getAllUsers("")).thenReturn(List.of());
        assertTrue(this.mockMvc.perform(get("/api/users")).andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals("[]"));
    }

    @Test
    void getUsersWhenThereAreUsersShouldReturnListOfUserDTO() throws Exception {
        when(userService.getAllUsers("")).thenReturn(List.of(new UserDTO(1,"stefanoss", "stefano", "chen", "bio", Instant.now()),
                new UserDTO(2, "mario", "mario", "bros", "mario", Instant.now())));
        this.mockMvc.perform(get("/api/users")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("stefanoss"))
                .andExpect(jsonPath("$[1].username").value("mario"));
    }

    @Test
    void getUsersWhereThereAreNotMatchingUsersShouldReturnEmptyList() throws Exception {
        when(userService.getAllUsers("")).thenReturn(List.of());
        assertTrue(this.mockMvc.perform(get("/api/users?q=mario")).andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals("[]"));

    }

    @Test
    void getUsersWhereThereAreMatchingUsersShouldReturnListOfMatches() throws Exception {
        when(userService.getAllUsers("")).thenReturn(List.of(new UserDTO(2, "mario", "mario", "bros", "mario", Instant.now())));
        this.mockMvc.perform(get("/api/users")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("mario"));
    }


    @Test
    void getUserByIdWithInvalidIdShouldReturnCode400() throws Exception {
        when(userService.getUser(any())).thenThrow(InvalidIdException.class);
        this.mockMvc.perform(get("/api/users/ciao")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void getUserByIdWithValidIdShouldReturnUserDetails() throws Exception {
        when(userService.getUser(1)).thenReturn(new UserDTO(1,"stefanoss", "stefano", "chen", "bio", Instant.now()));
        this.mockMvc.perform(get("/api/users/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("stefanoss"));
    }
}