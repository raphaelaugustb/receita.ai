
package com.leah.receitas.ai.controller;

import com.leah.receitas.ai.dto.user.UserPublicInfo;
import com.leah.receitas.ai.dto.user.UserRequest;
import com.leah.receitas.ai.dto.user.UserResponse;
import com.leah.receitas.ai.handler.UserHandler;
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
    public ResponseEntity<UserResponse> registerNewUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequest));
    }

    // Deletar a conta de um usuário
    @DeleteMapping("{userId}/account")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Receber os dados públicos do usuário (Receitas que são públicas e o seu nome)
    @GetMapping("{username}/profile")
    public ResponseEntity<UserPublicInfo> getUserPublicInfo(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getUserPublicInfo(username));
    }

    //Receber todos os dados do usuário
    @GetMapping( "{userId}/account")
    public ResponseEntity<UserResponse> getUserPrivateInfo (@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getUserInfo(userId));
    }
    // Alterar dados da conta do usuário
    @PutMapping("{userId}/account")
    public ResponseEntity<UserResponse> updateUserInfo(@PathVariable UUID userId, @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateUser(userId, userRequest));
    }
}