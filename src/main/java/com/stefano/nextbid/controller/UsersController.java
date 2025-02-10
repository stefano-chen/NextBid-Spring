package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/bio")
    public ResponseEntity<?> updateUserBio() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
