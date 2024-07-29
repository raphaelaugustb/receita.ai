package com.leah.receitas.ai.service;

import com.leah.receitas.ai.dto.user.UserPublicInfo;
import com.leah.receitas.ai.dto.user.UserRequest;
import com.leah.receitas.ai.dto.user.UserResponse;
import com.leah.receitas.ai.entity.Recipe;
import com.leah.receitas.ai.entity.User;
import com.leah.receitas.ai.exception.InvalidRequestException;
import com.leah.receitas.ai.exception.MissingFieldsException;
import com.leah.receitas.ai.exception.UserNotFoundException;
import com.leah.receitas.ai.repository.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {
    //TODO
    // Implementar métodos CRUD do usuário
    // Verificar Request com DTO
    // Listas as listas do usuário pelo nome e permitir que apenas liste a lista publica
    // Listar o perfil do usuário pelo nome
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User verifyUser(UUID userId) {
        User user =  userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new UserNotFoundException("User not Found");
        return user;
    }
    public UserResponse createUser(UserRequest userRequest) {
       User user = new User();
       user.setUsername(userRequest.username());
       user.setPassword(userRequest.password());
       user.setEmail(userRequest.email());
       user.setRecipeList(new ArrayList<>());
       user.setSavedRecipeList(new ArrayList<>());
       user.setTempRecipeList(new ArrayList<>());

       if (user.getEmail() != null && user.getPassword()!= null && user.getUsername() != null){
            userRepository.save(user);
       } else {
           throw new MissingFieldsException("Some of the fields are missing");
       }

       return new UserResponse(user.getUsername(), user.getEmail(), user.getPassword(), user.getRecipeList(), user.getSavedRecipeList());
    }
    public void deleteUser(UUID userId) {
      User user = verifyUser(userId);
      userRepository.delete(user);
    }
    public UserResponse getUserInfo(UUID userId){
        User user = verifyUser(userId);
        return new UserResponse(user.getUsername(), user.getEmail(), user.getPassword(), user.getRecipeList(), user.getSavedRecipeList());
    }
     public UserPublicInfo getUserPublicInfo(String username){
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UserNotFoundException("User not Found");
         List<Recipe> recipeList = user.getRecipeList().stream().filter(recipe -> !recipe.getIsPrivate()).toList();
        return new UserPublicInfo(username, recipeList);

     }
     public UserResponse updateUser(UUID userId, UserRequest userRequest) {
        User user = verifyUser(userId);
         if (userRequest == null)
             throw new InvalidRequestException("Request cannot be null");
        user.setUsername(userRequest.username());
        user.setPassword(userRequest.password());
        user.setEmail(userRequest.email());
         return new UserResponse(user.getUsername(), user.getEmail(), user.getPassword(), user.getRecipeList(), user.getSavedRecipeList());
     }
}