package com.stefano.nextbid.service;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.stefano.nextbid.dto.SigninBody;
import com.stefano.nextbid.dto.SignupBody;
import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidCredentialsException;
import com.stefano.nextbid.exceptions.UsernameAlreadyExistsException;
import com.stefano.nextbid.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final SessionManager sessionManager;
    private final BcryptFunction bcryptFunction;

    @Autowired
    public AuthService(UserMapper userMapper, UserRepository userRepository, SessionManager sessionManager, BcryptFunction bcryptFunction) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
        this.bcryptFunction = bcryptFunction;
    }

    public UserDTO signup(SignupBody body) throws UsernameAlreadyExistsException {
        User user = userMapper.mapToUser(body);
        userRepository.findUserByUsername(user.getUsername()).ifPresent(value -> {
            throw new UsernameAlreadyExistsException();
        });
        Hash hash = Password.hash(user.getPassword()).with(bcryptFunction);
        user.setPassword(hash.getResult());
        User savedUser = userRepository.save(user);
        sessionManager.setUserId(savedUser.get_id());
        return userMapper.mapToUserDTO(savedUser);
    }

    public UserDTO signin(SigninBody body) throws InvalidCredentialsException {
        User user = userRepository.findUserByUsername(body.username()).orElseThrow(InvalidCredentialsException::new);
        if (!(Password.check(body.password(), user.getPassword()).with(bcryptFunction))) {
            throw new InvalidCredentialsException();
        }
        sessionManager.setUserId(user.get_id());
        return userMapper.mapToUserDTO(user);
    }

    public void logout() {
        sessionManager.destroy();
    }

}
