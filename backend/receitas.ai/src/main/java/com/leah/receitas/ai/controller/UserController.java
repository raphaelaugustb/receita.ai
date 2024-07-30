
package com.leah.receitas.ai.controller;

import com.leah.receitas.ai.dto.user.UserPublicInfo;
import com.leah.receitas.ai.dto.user.UserRequest;
import com.leah.receitas.ai.dto.user.UserResponse;
import com.leah.receitas.ai.entity.User;
import com.leah.receitas.ai.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @DeleteMapping("{userId}/account")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Account successfully deleted");
    }

    @GetMapping("{username}/profile")
    public ResponseEntity<UserPublicInfo> userPublicProfile(@PathVariable String username) {
        UserPublicInfo userPublicInfo = userService.getUserPublicInfo(username);
        return ResponseEntity.status(HttpStatus.FOUND).body(userPublicInfo);
    }
    @GetMapping( "{userId}/account")
    public ResponseEntity<UserResponse> userAccount (@PathVariable UUID userId) {
        UserResponse userResponse = userService.getUserInfo(userId);
        return ResponseEntity.status(HttpStatus.FOUND).body(userResponse);
    }
}
