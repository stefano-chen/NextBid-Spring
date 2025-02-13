package com.stefano.nextbid.service;

import com.password4j.BcryptFunction;
import com.password4j.Password;
import com.password4j.types.Bcrypt;
import com.stefano.nextbid.dto.SigninBody;
import com.stefano.nextbid.dto.SignupBody;
import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidCredentialsException;
import com.stefano.nextbid.exceptions.UsernameAlreadyExistsException;
import com.stefano.nextbid.repo.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private final UserMapper userMapper = mock(UserMapper.class);

    private final UserRepository userRepository = mock(UserRepository.class);

    private final SessionManager sessionManager = mock(SessionManager.class);

    private final BcryptFunction bcryptFunction = BcryptFunction.getInstance(Bcrypt.B, 12);

    private final AuthService authService = new AuthService(userMapper, userRepository, sessionManager, bcryptFunction);


    @Test
    void signupWithNullArgShouldThrowException() {
        when(userMapper.mapToUser(null)).thenReturn(null);
        when(userRepository.findUserByUsername(null)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> authService.signup(null));
    }

    @Test
    void signupWithValidBodyShouldReturnUserDTO() {

        SignupBody body = new SignupBody("stefano", "chen", "stefanoss", "hello");
        User user = new User("stefanoss", "stefano", "chen", "", "body");
        UserDTO userDTO = new UserDTO(1, "stefanoss", "stefano", "chen", "", Instant.now());
        when(userMapper.mapToUser(body)).thenReturn(user);
        user.set_id(1);
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.mapToUserDTO(user)).thenReturn(userDTO);

        UserDTO response = authService.signup(body);

        assertEquals("stefano", response.name());
        assertEquals("chen", response.surname());
        assertEquals("stefanoss", response.username());
    }

    @Test
    void signupWithExistingUsernameShouldThrowException() {

        SignupBody body = new SignupBody("stefano", "chen", "stefanoss", "hello");
        User user = new User("stefanoss", "stefano", "chen", "", "body");
        UserDTO userDTO = new UserDTO(1, "stefanoss", "stefano", "chen", "", Instant.now());
        when(userMapper.mapToUser(body)).thenReturn(user);
        user.set_id(1);
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.mapToUserDTO(user)).thenReturn(userDTO);

        assertThrows(UsernameAlreadyExistsException.class, () -> authService.signup(body));
    }

    @Test
    void signinWithNullArgShouldThrowException() {
        when(userRepository.findUserByUsername(null)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> authService.signup(null));
    }

    @Test
    void signinWithValidBodyShouldReturnUserDTO() {
        SigninBody body = new SigninBody("stefanoss", "hello");
        User user = new User("stefanoss", "stefano", "chen", "bio", Password.hash("hello").with(bcryptFunction).getResult());
        UserDTO mappedUser = new UserDTO(1, "stefanoss", "stefano", "chen", "bio", Instant.now());
        when(userRepository.findUserByUsername(body.username())).thenReturn(Optional.of(user));
        when(userMapper.mapToUserDTO(user)).thenReturn(mappedUser);

        UserDTO response = authService.signin(body);

        assertEquals("stefanoss", response.username());
        assertEquals("stefano", response.name());
        assertEquals("chen", response.surname());
        assertEquals("bio", response.bio());

//        assertThrows(NullPointerException.class, () -> authService.signup(null));
    }

    @Test
    void signinWithInvalidUsernameShouldThrow() {
        SigninBody body = new SigninBody("mario", "hello");
        when(userRepository.findUserByUsername(body.username())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.signin(body));
    }

    @Test
    void signinWithInvalidPasswordShouldThrow() {
        SigninBody body = new SigninBody("stefanoss", "hello");
        User user = new User("stefanoss", "stefano", "chen", "bio", Password.hash("password").with(bcryptFunction).getResult());
        UserDTO mappedUser = new UserDTO(1, "stefanoss", "stefano", "chen", "bio", Instant.now());
        when(userRepository.findUserByUsername(body.username())).thenReturn(Optional.of(user));
        when(userMapper.mapToUserDTO(user)).thenReturn(mappedUser);
        assertThrows(InvalidCredentialsException.class, () -> authService.signin(body));
    }

}