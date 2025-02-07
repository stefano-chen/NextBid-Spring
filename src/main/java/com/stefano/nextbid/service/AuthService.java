package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.SignupBody;
import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public UserDTO signup(SignupBody body) {
        User user = userMapper.mapToUser(body);
        User savedUser = userRepository.save(user);
        return userMapper.mapToUserDTO(savedUser);
    }
}
