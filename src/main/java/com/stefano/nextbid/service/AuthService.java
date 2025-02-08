package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.SignupBody;
import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.UsernameAlreadyExistsException;
import com.stefano.nextbid.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final SessionManager sessionManager;


    @Autowired
    public AuthService(UserMapper userMapper, UserRepository userRepository, SessionManager sessionManager) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
    }

    public UserDTO signup(SignupBody body) throws UsernameAlreadyExistsException {
        User user = userMapper.mapToUser(body);
        userRepository.findUserByUsername(user.getUsername()).ifPresent(value -> {throw new UsernameAlreadyExistsException();});
        User savedUser = userRepository.save(user);
        sessionManager.setUserId(savedUser.getId());
        return userMapper.mapToUserDTO(savedUser);
    }
}
