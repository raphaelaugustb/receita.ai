
package com.leah.receitas.ai.controller;

import com.leah.receitas.ai.dto.user.UserPublicInfo;
import com.leah.receitas.ai.dto.user.UserRequest;
import com.leah.receitas.ai.dto.user.UserResponse;
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

    //Criar um novo usuário
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    // Deletar a conta de um usuário
    @DeleteMapping("{userId}/account")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Account successfully deleted");
    }

    //Receber os dados públicos do usuário (Receitas que são públicas e o seu nome)
    @GetMapping("{username}/profile")
    public ResponseEntity<UserPublicInfo> userPublicProfile(@PathVariable String username) {
        UserPublicInfo userPublicInfo = userService.getUserPublicInfo(username);
        return ResponseEntity.status(HttpStatus.FOUND).body(userPublicInfo);
    }

    //Receber todos os dados do usuário
    @GetMapping( "{userId}/account")
    public ResponseEntity<UserResponse> userAccount (@PathVariable UUID userId) {
        UserResponse userResponse = userService.getUserInfo(userId);
        return ResponseEntity.status(HttpStatus.FOUND).body(userResponse);
    }
    // Alterar dados da conta do usuário
    @PutMapping("{userId}/account")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId, @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userId, userRequest));
    }
}