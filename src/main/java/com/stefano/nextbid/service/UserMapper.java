package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.SignupBody;
import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User mapToUser(SignupBody body) {
        if (body == null)
            return null;
        return new User(body.username(), body.name(), body.surname(), "", body.password());
    }


    public UserDTO mapToUserDTO(User user) {
        if (user == null)
            return null;
        return new UserDTO(user.get_id(), user.getUsername(), user.getName(), user.getSurname(), user.getBio(), user.getCreatedAt());
    }
}
