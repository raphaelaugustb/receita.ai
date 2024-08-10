package com.leah.receitas.ai.service;

import com.leah.receitas.ai.controller.RecipeController;
import com.leah.receitas.ai.dto.recipe.RecipeRequest;
import com.leah.receitas.ai.dto.recipe.RecipeResponse;
import com.leah.receitas.ai.entity.Recipe;
import com.leah.receitas.ai.entity.User;
import com.leah.receitas.ai.exception.MissingFieldsException;
import com.leah.receitas.ai.exception.RecipeNotFoundException;
import com.leah.receitas.ai.repository.RecipeRepository;
import com.leah.receitas.ai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecipeService {
    private final RecipeController recipeController;
    //TODO
    // Implementar IA
    UserService userService;
    UserRepository userRepository;
    RecipeRepository recipeRepository;

    private Recipe verifyRecipe(long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        if (recipe == null && !recipe.getIsPrivate()) throw new RecipeNotFoundException("Recipe not found");
        return recipe;
    }

    public RecipeService(UserService userService, UserRepository userRepository, RecipeRepository recipeRepository, RecipeController recipeController) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.recipeController = recipeController;
    }
    private RecipeRequest verifyRecipeRequest(RecipeRequest recipeRequest) {
        if(recipeRequest.recipeName() == null || recipeRequest.instructions() == null || recipeRequest.ingredients() == null || recipeRequest.isPrivate() == null)
            throw new MissingFieldsException("Missing required fields");
        return recipeRequest;
    }
    private Recipe createNewRecipeFromRequest(RecipeRequest recipeRequest, User user) {
        Recipe recipe = new Recipe();
        RecipeRequest verifyRecipeRequest = verifyRecipeRequest(recipeRequest);
        recipe.setRecipeName(verifyRecipeRequest.recipeName());
        recipe.setUsernameRecipe(user.getUsername());
        recipe.setIsPrivate(verifyRecipeRequest.isPrivate());
        recipe.setIngredients(verifyRecipeRequest.ingredients());
        recipe.setInstructions(verifyRecipeRequest.instructions());
        return recipe;
    }

    public void removeRecipeSaved(UUID userId, long recipeId) {
        User user = userService.verifyUser(userId);
        user.getSavedRecipeList().removeIf(id -> id == recipeId);
        userRepository.save(user);
    }

    public List<RecipeResponse> listSavedRecipes(UUID userId) {
        List<RecipeResponse> tempSavedRecipes = new ArrayList<>();
        User user = userService.verifyUser(userId);
        for (long id : user.getSavedRecipeList()) {
            Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
            if (recipe != null) {
                RecipeResponse response = new RecipeResponse(recipe.getRecipeName(), recipe.getIngredients(), recipe.getInstructions(), recipe.getIsPrivate(), recipe.getUsernameRecipe());
                tempSavedRecipes.add(response);
            }
        }
        return tempSavedRecipes;
    }

    public void saveRecipeFromUsers(UUID userId, long recipeId) {
        User user = userService.verifyUser(userId);
        Recipe recipe = verifyRecipe(recipeId);
        user.getSavedRecipeList().add(recipe.getId());
        userRepository.save(user);
    }

    public List<RecipeResponse> getRecipesByNamePublic(String recipeName) {
        List<Recipe> recipeList = recipeRepository.findByRecipeName(recipeName);
        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        recipeList.stream().filter(recipe -> !recipe.getIsPrivate()).toList().forEach(recipe -> {
            RecipeResponse recipeResponse = new RecipeResponse(recipe.getRecipeName(), recipe.getInstructions(), recipe.getIngredients(), recipe.getIsPrivate(), recipe.getUsernameRecipe());
            recipeResponseList.add(recipeResponse);
        });
        if (recipeResponseList.isEmpty())
            throw new RecipeNotFoundException("None recipes matches with name: " + recipeName);
        return recipeResponseList;
    }

    public List<RecipeResponse> getUserRecipesByName(UUID userId, String recipeName) {
        User user = userService.verifyUser(userId);
        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        user.getRecipeList().stream().filter(recipe -> recipe.getRecipeName().equalsIgnoreCase(recipeName)).toList().forEach(recipe -> {
            RecipeResponse recipeResponse = new RecipeResponse(recipe.getRecipeName(), recipe.getInstructions(), recipe.getIngredients(), recipe.getIsPrivate(), recipe.getUsernameRecipe());
            recipeResponseList.add(recipeResponse);
        });
        if (recipeResponseList.isEmpty())
            throw new RecipeNotFoundException("None recipes matches with name: " + recipeName);
        return recipeResponseList;
    }

    public RecipeResponse updateRecipeInfo(UUID userId, long recipeId, RecipeRequest recipeRequest) {
        User user = userService.verifyUser(userId);
        RecipeRequest verifyRecipe = verifyRecipeRequest(recipeRequest);
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        user.getRecipeList().remove(recipe);
        recipe.setIngredients(verifyRecipe.ingredients());
        recipe.setInstructions(verifyRecipe.instructions());
        recipe.setIsPrivate(verifyRecipe.isPrivate());
        recipe.setRecipeName(verifyRecipe.recipeName());
        recipeRepository.save(recipe);
        user.getRecipeList().add(recipe);

        userRepository.save(user);

        return new RecipeResponse(recipeRequest.recipeName(), recipeRequest.ingredients(), recipeRequest.instructions(), recipeRequest.isPrivate(), user.getUsername());
    }

    public RecipeResponse createRecipe(RecipeRequest recipeRequest, UUID userId) {
        User user = userService.verifyUser(userId);
        Recipe recipe = createNewRecipeFromRequest(recipeRequest, user);
        recipeRepository.save(recipe);
        user.getRecipeList().add(recipe);
        userRepository.save(user);
        return new RecipeResponse(recipeRequest.recipeName(), recipeRequest.ingredients(), recipeRequest.instructions(), recipeRequest.isPrivate(), user.getUsername());
    }


    public void deleteRecipe(UUID userId, long recipeId) {
        User user = userService.verifyUser(userId);
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        user.getRecipeList().remove(recipe);
        recipeRepository.deleteById(recipeId);
        userRepository.save(user);
    }
}