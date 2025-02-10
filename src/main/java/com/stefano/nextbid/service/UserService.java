package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> getAllUsers(String q) throws IllegalArgumentException {
        if (q == null) {
            throw new IllegalArgumentException();
        }
        if (q.isEmpty()) {
            return userRepository.findAll()
                    .stream().map(userMapper::mapToUserDTO).toList();
        }
        return userRepository.findAllByUsernameContainingIgnoreCase(q).stream().map(userMapper::mapToUserDTO).toList();
    }

    public UserDTO getUser(Integer id) throws IllegalArgumentException, InvalidIdException {
        if (id == null)
            throw new IllegalArgumentException();

        User user = userRepository.findById(id).orElseThrow(InvalidIdException::new);
        return userMapper.mapToUserDTO(user);
    }
}
