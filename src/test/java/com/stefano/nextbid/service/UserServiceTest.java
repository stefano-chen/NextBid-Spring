package com.stefano.nextbid.service;

import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserMapper userMapper;

    private UserService userService = new UserService(userRepository, userMapper);

    @Test
    void getAllUsersWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {userService.getAllUsers(null);});
    }

    void getUserWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {userService.getUser(null);});
    }

    void getUserWithInvalidIdShouldThrow() {
        assertThrows(InvalidIdException.class, () -> {userService.getUser(99989);});
    }

    void getUserWithValidIdShouldReturnFullDetail() {
        assertThrows(InvalidIdException.class, () -> {userService.getUser(1);});
    }

}