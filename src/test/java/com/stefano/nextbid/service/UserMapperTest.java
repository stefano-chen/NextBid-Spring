package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.SignupBody;
import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void mapToUserWithNullArgShouldReturnNull() {
        assertNull(userMapper.mapToUser(null));
    }

    @Test
    void mapToUserWithValidArgShouldReturnValidUser() {
        SignupBody body = new SignupBody("stefano", "chen", "stefanoss", "password");
        User mappedUser = userMapper.mapToUser(body);

        assertEquals("stefano", mappedUser.getName());
        assertEquals("chen", mappedUser.getSurname());
        assertEquals("stefanoss", mappedUser.getUsername());
        assertEquals("password", mappedUser.getPassword());
    }

    @Test
    void mapToUserDTOWithNullArgShouldReturnNull() {
        assertNull(userMapper.mapToUserDTO(null));
    }

    @Test
    void mapToUserDTOWithValidArgShouldReturnValidUserDTO() {
        User user = new User("stefanoss", "stefano", "chen", "bio", "password");
        UserDTO mappedUser = userMapper.mapToUserDTO(user);

        assertEquals("stefanoss", mappedUser.username());
        assertEquals("stefano", mappedUser.name());
        assertEquals("chen", mappedUser.surname());
        assertEquals("bio", mappedUser.bio());
    }
}