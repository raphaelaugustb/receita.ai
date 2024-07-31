package com.leah.receitas.ai.service;

import com.leah.receitas.ai.dto.recipe.RecipeRequest;
import com.leah.receitas.ai.dto.recipe.RecipeResponse;
import com.leah.receitas.ai.entity.Recipe;
import com.leah.receitas.ai.entity.User;
import com.leah.receitas.ai.exception.MissingFieldsException;
import com.leah.receitas.ai.exception.RecipeNotFoundException;
import com.leah.receitas.ai.repository.RecipeRepository;
import com.leah.receitas.ai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecipeService {
    //TODO
    // Implementar função de criação de receitas publicas ou privadas
    // Implementar métodos crud
    // Alimentar API com receitas externas
    // Permitir usuário buscar receitas pelo nome
    // Listar apenas receitas não privadas
    // Permitir o usuário Salvar receitas de terceiros
    // Implementar IA
    // Verificar dados com DTO. mapear saídas e entradas
    UserService userService;
    UserRepository userRepository;
    RecipeRepository recipeRepository;

    public RecipeService(UserService userService, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    public RecipeResponse createRecipe(RecipeRequest recipeRequest, UUID userId) {
        User user = userService.verifyUser(userId);
        Recipe recipe = new Recipe();
        recipe.setRecipeName(recipeRequest.recipeName());
        recipe.setUsernameRecipe(user.getUsername());
        recipe.setIsPrivate(recipeRequest.isPrivate());
        recipe.setIngredients(recipeRequest.ingredients());
        recipe.setInstructions(recipeRequest.instructions());
        if (recipe.getRecipeName() == null || recipe.getInstructions() == null || recipe.getIngredients() == null)
            throw new MissingFieldsException("Some of the field are missing");
        userRepository.save(user);
        user.getRecipeList().add(recipe);
        recipeRepository.save(recipe);
        return new RecipeResponse(recipeRequest.recipeName(),recipeRequest.ingredients(),recipeRequest.instructions(),recipeRequest.isPrivate(), user.getUsername());
    }

    public void deleteRecipe(UUID userId, long recipeId) {
        User user = userService.verifyUser(userId);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        if (recipe == null && recipe.getUsernameRecipe().equalsIgnoreCase(user.getUsername()))
            throw new RecipeNotFoundException("Recipe not found");
        user.getRecipeList().remove(recipe);
        userRepository.save(user);
        recipeRepository.deleteById(recipe.getId());
    }
 }