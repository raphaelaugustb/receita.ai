package com.leah.receitas.ai.handler;

import com.leah.receitas.ai.dto.user.UserPublicInfo;
import com.leah.receitas.ai.dto.user.UserRequest;
import com.leah.receitas.ai.dto.user.UserResponse;
import com.leah.receitas.ai.exception.UserNotFoundException;
import com.leah.receitas.ai.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserHandler {
    UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public ResponseEntity<UserResponse> createUserHandler(UserRequest userRequest){
        try {
            UserResponse userResponse = userService.createUser(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    public ResponseEntity<String> deleteAccountHandler( UUID userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    public ResponseEntity<UserPublicInfo> getPublicInfoHandler(String username){
        try {
            UserPublicInfo userPublicInfo = userService.getUserPublicInfo(username);
            return ResponseEntity.status(HttpStatus.FOUND).body(userPublicInfo);
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public ResponseEntity<UserResponse> getPrivateInfoHandler(UUID userId){
        UserResponse userResponse = userService.getUserInfo(userId);
        return ResponseEntity.status(HttpStatus.FOUND).body(userResponse);
    }
    public ResponseEntity<UserResponse> updateUserInfoHandler(UUID userId, UserRequest userRequest){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userId, userRequest));
        } catch (Exception e) {
            if (e.getClass().equals(UserNotFoundException.class))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
