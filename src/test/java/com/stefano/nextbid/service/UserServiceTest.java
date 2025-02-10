package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.exceptions.NotAuthenticatedException;
import com.stefano.nextbid.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserRepository userRepository = mock(UserRepository.class);

    private UserMapper userMapper = mock(UserMapper.class);

    private SessionManager sessionManager = mock(SessionManager.class);

    private UserService userService = new UserService(userRepository, userMapper, sessionManager);

    @Test
    void getAllUsersWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {userService.getAllUsers(null);});
    }

    @Test
    void getUserWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {userService.getUser(null);});
    }

    @Test
    void getUserWithInvalidIdShouldThrow() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> {userService.getUser(99989);});
    }

    @Test
    void getUserWithValidIdShouldReturnFullDetail() {
        User user = new User("stefanoss", "stefano", "chen", "bio", "password");
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userMapper.mapToUserDTO(user)).thenReturn(new UserDTO(1,"stefanoss", "stefano", "chen", "bio", Instant.now()));

        UserDTO userDTO = userService.getUser(1);
        assertEquals("stefanoss", userDTO.username());
        assertEquals("stefano", userDTO.name());
        assertEquals("chen", userDTO.surname());
        assertEquals("bio", userDTO.bio());
    }

    @Test
    void updateBioWithValidBodyAndAuthenticatedShouldSuccess() {
        when(sessionManager.isAuthenticated()).thenReturn(true);
        when(sessionManager.getUserId()).thenReturn(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(new User("stefanoss", "stefano", "chen", "bio", "password")));
        assertDoesNotThrow(() -> {userService.updateBio("ciao");});
    }

    @Test
    void updateBioWithValidBodyAndNotAuthenticatedShouldThrow() {
        when(sessionManager.isAuthenticated()).thenReturn(false);
        assertThrows(NotAuthenticatedException.class,() -> {userService.updateBio("ciao");});
    }

    @Test
    void updateBioWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {userService.updateBio(null);});
    }

}