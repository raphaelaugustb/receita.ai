package com.leah.receitas.ai.service;

import com.leah.receitas.ai.dto.user.UserPublicInfo;
import com.leah.receitas.ai.dto.user.UserRequest;
import com.leah.receitas.ai.dto.user.UserResponse;
import com.leah.receitas.ai.entity.Recipe;
import com.leah.receitas.ai.entity.User;
import com.leah.receitas.ai.exception.InvalidRequestException;
import com.leah.receitas.ai.exception.UserNotFoundException;
import com.leah.receitas.ai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    //TODO
    // Implementar segurança e autenticação via JWT
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User verifyUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserResponse createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.username());
        user.setPassword(userRequest.password());
        user.setEmail(userRequest.email());
        user.setRecipeList(new ArrayList<>());
        user.setSavedRecipeList(new ArrayList<>());
        user.setTempRecipeList(new ArrayList<>());

        if (userRequest.email() == null || userRequest.password() == null || userRequest.username() == null)
            throw new InvalidRequestException("Some of the fields are missing");

        userRepository.save(user);

        return new UserResponse(user.getUsername(), user.getEmail(), user.getPassword(), user.getRecipeList(), user.getSavedRecipeList());
    }

    public void deleteUser(UUID userId) {
        User user = verifyUser(userId);
        userRepository.delete(user);
    }

    public UserResponse getUserInfo(UUID userId) {
        User user = verifyUser(userId);
        return new UserResponse(user.getUsername(), user.getEmail(), user.getPassword(), user.getRecipeList(), user.getSavedRecipeList());
    }

    public UserPublicInfo getUserPublicInfo(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Recipe> recipeList = user.getRecipeList().stream().filter(recipe -> !recipe.getIsPrivate()).toList();
        return new UserPublicInfo(username, recipeList);
    }

    public UserResponse updateUser(UUID userId, UserRequest userRequest) {
        User user = verifyUser(userId);
        if (userRequest.email() == null || userRequest.password() == null || userRequest.username() == null)
            throw new InvalidRequestException("Request cannot be null");
        user.setUsername(userRequest.username());
        user.setPassword(userRequest.password());
        user.setEmail(userRequest.email());
        return new UserResponse(user.getUsername(), user.getEmail(), user.getPassword(), user.getRecipeList(), user.getSavedRecipeList());
    }
}