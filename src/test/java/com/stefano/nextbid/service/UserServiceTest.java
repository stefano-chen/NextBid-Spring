package com.stefano.nextbid.service;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.exceptions.NotAuthenticatedException;
import com.stefano.nextbid.repo.AuctionRepository;
import com.stefano.nextbid.repo.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final AuctionRepository auctionRepository = mock(AuctionRepository.class);
    private final UserMapper userMapper = mock(UserMapper.class);
    private final SessionManager sessionManager = mock(SessionManager.class);
    private final AuctionMapper auctionMapper = mock(AuctionMapper.class);
    private final UserService userService = new UserService(
            userRepository, auctionRepository, userMapper, sessionManager, auctionMapper
    );

    @Test
    void getAllUsersWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getAllUsers(null);
        });
    }

    @Test
    void getUserWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUser(null);
        });
    }

    @Test
    void getUserWithInvalidIdShouldThrow() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(InvalidIdException.class, () -> {
            userService.getUser(99989);
        });
    }

    @Test
    void getUserWithValidIdShouldReturnFullDetail() {
        User user = new User("stefanoss", "stefano", "chen", "bio", "password");
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userMapper.mapToUserDTO(user)).thenReturn(
                new UserDTO(1, "stefanoss", "stefano", "chen", "bio", Instant.now())
        );

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
        when(userRepository.findById(1)).thenReturn(
                Optional.of(new User(
                        "stefanoss", "stefano", "chen", "bio", "password"
                ))
        );
        assertDoesNotThrow(() -> {
            userService.updateBio("ciao");
        });
    }

    @Test
    void updateBioWithValidBodyAndNotAuthenticatedShouldThrow() {
        when(sessionManager.isAuthenticated()).thenReturn(false);
        assertThrows(NotAuthenticatedException.class, () -> {
            userService.updateBio("ciao");
        });
    }

    @Test
    void updateBioWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateBio(null);
        });
    }

    @Test
    void getUserAuctionWithValidIdShouldReturnList() {
        Integer id = 1;
        User user = new User(id);
        when(auctionRepository.findAllByOwner(any())).thenReturn(List.of(
                new Auction("auction", "auction", Instant.now(), 10.0, user, null)
                )
        );
        when(auctionMapper.mapToAuctionDTO(any())).thenCallRealMethod();

        List<AuctionDTO> auctionList = userService.getUserAuctions(id);

        assertEquals(user.getId(), auctionList.get(0).owner());
    }

    @Test
    void getUserAuctionWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserAuctions(null));
    }


    @Test
    void getUserWonAuctionWithValidIdShouldReturnList() {
        Integer id = 1;
        User user = new User(id);
        when(auctionRepository.findAllByWinner(any())).thenReturn(List.of(
                new Auction("auction", "auction", Instant.now(), 10.0, user, null)
                )
        );
        when(auctionMapper.mapToAuctionDTO(any())).thenCallRealMethod();

        List<AuctionDTO> auctionList = userService.getUserWonAuctions(id);

        assertEquals(user.getId(), auctionList.get(0).owner());
    }

    @Test
    void getUserWonAuctionWithNullArgShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserWonAuctions(null));
    }
}