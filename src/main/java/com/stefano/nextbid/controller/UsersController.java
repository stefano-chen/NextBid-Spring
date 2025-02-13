package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.exceptions.NotAuthenticatedException;
import com.stefano.nextbid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsersFilterByQuery(@RequestParam(value = "q", defaultValue = "", required = false) String q) {
        List<UserDTO> users = userService.getAllUsers(q);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetailById(@PathVariable Integer id) {
        UserDTO user = userService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}/auctions")
    public ResponseEntity<?> getUserAuctions(@PathVariable Integer id) {
        List<AuctionDTO> auctionList = userService.getUserAuctions(id);
        return new ResponseEntity<>(auctionList, HttpStatus.OK);
    }

    @GetMapping("/{id}/auctions/won")
    public ResponseEntity<?> getUserWonAuctions(@PathVariable Integer id) {
        List<AuctionDTO> auctionList = userService.getUserWonAuctions(id);
        return new ResponseEntity<>(auctionList, HttpStatus.OK);
    }

    @PutMapping("/bio")
    public ResponseEntity<?> updateUserBio(@RequestBody String body) {
        userService.updateBio(body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler({InvalidIdException.class, NotAuthenticatedException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleInvalidId(Exception e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
