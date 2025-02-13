package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.exceptions.NotAuthenticatedException;
import com.stefano.nextbid.repo.AuctionRepository;
import com.stefano.nextbid.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AuctionRepository auctionRepository;

    private final UserMapper userMapper;

    private final SessionManager sessionManager;
    private final AuctionMapper auctionMapper;

    @Autowired
    public UserService(UserRepository userRepository, AuctionRepository auctionRepository, UserMapper userMapper, SessionManager sessionManager, AuctionMapper auctionMapper) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.userMapper = userMapper;
        this.sessionManager = sessionManager;
        this.auctionMapper = auctionMapper;
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

    public void updateBio(String bio) throws NotAuthenticatedException, IllegalArgumentException, InvalidIdException {
        if (bio == null)
            throw new IllegalArgumentException();
        if (!sessionManager.isAuthenticated())
            throw new NotAuthenticatedException();
        Integer userId = sessionManager.getUserId();
        User user = userRepository.findById(userId).orElseThrow(InvalidIdException::new);
        user.setBio(bio);
        userRepository.save(user);
    }

    public List<AuctionDTO> getUserAuctions(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();

        User user = new User(id);
        return auctionRepository.findAllByOwner(user).stream().map(auctionMapper::mapToAuctionDTO).toList();
    }

    public List<AuctionDTO> getUserWonAuctions(Integer id) {
        if (id == null)
            throw new IllegalArgumentException();

        User user = new User(id);
        return auctionRepository.findAllByWinner(user).stream().map(auctionMapper::mapToAuctionDTO).toList();
    }
}
