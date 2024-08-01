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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecipeService {
    //TODO
    // Implementar IA
    UserService userService;
    UserRepository userRepository;
    RecipeRepository recipeRepository;

    public RecipeService(UserService userService, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }
    public void removeRecipeSaved(UUID userId, long recipeId){
        User user = userService.verifyUser(userId);
        user.getSavedRecipeList().removeIf(id -> id == recipeId);
        userRepository.save(user);
    }
    public List<RecipeResponse> listSavedRecipes(UUID userId){
        List<RecipeResponse> tempSavedRecipes = new ArrayList<>();
        User user = userService.verifyUser(userId);
        for (long id : user.getSavedRecipeList()){
            Recipe recipe = recipeRepository.findById(id).orElse(null);
            if (recipe != null){
                RecipeResponse response = new RecipeResponse(recipe.getRecipeName(), recipe.getIngredients(), recipe.getInstructions(), recipe.getIsPrivate(), recipe.getUsernameRecipe());
                tempSavedRecipes.add(response);
            }
        }
        return tempSavedRecipes;
    }
    public RecipeResponse saveRecipeFromUsers(UUID userId, long recipeId){
        User user = userService.verifyUser(userId);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        if (recipe == null && !recipe.getIsPrivate())
            throw new RecipeNotFoundException("Recipe not found");
        user.getSavedRecipeList().add(recipe.getId());
        userRepository.save(user);
        return new RecipeResponse(recipe.getRecipeName(), recipe.getIngredients(), recipe.getInstructions(), recipe.getIsPrivate(), recipe.getUsernameRecipe());
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
    public List<RecipeResponse> getUserRecipesByName(UUID userId, String recipeName){
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
        Recipe newRecipe = getRecipe(recipeRequest, user);
        for (Recipe recipe : user.getRecipeList()) {
            if (recipe.getId() == recipeId) {
                recipe.setIngredients(newRecipe.getIngredients());
                recipe.setInstructions(newRecipe.getInstructions());
                recipe.setIsPrivate(newRecipe.getIsPrivate());
                recipe.setRecipeName(newRecipe.getRecipeName());
                recipeRepository.save(recipe);
                userRepository.save(user);
                break;
            }
        }
        return new RecipeResponse(recipeRequest.recipeName(), recipeRequest.ingredients(), recipeRequest.instructions(), recipeRequest.isPrivate(), user.getUsername());
    }

    public RecipeResponse createRecipe(RecipeRequest recipeRequest, UUID userId) {
        User user = userService.verifyUser(userId);
        Recipe recipe = getRecipe(recipeRequest, user);
        recipeRepository.save(recipe);
        user.getRecipeList().add(recipe);
        userRepository.save(user);
        return new RecipeResponse(recipeRequest.recipeName(), recipeRequest.ingredients(), recipeRequest.instructions(), recipeRequest.isPrivate(), user.getUsername());
    }

    private Recipe getRecipe(RecipeRequest recipeRequest, User user) {
        Recipe recipe = new Recipe();
        recipe.setRecipeName(recipeRequest.recipeName());
        recipe.setUsernameRecipe(user.getUsername());
        recipe.setIsPrivate(recipeRequest.isPrivate());
        recipe.setIngredients(recipeRequest.ingredients());
        recipe.setInstructions(recipeRequest.instructions());
        if (recipe.getRecipeName() == null || recipe.getInstructions() == null || recipe.getIngredients() == null)
            throw new MissingFieldsException("Some of the field are missing");
        return recipe;
    }

    public void deleteRecipe(UUID userId, long recipeId) {
        User user = userService.verifyUser(userId);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        if (recipe == null && !(recipe.getUsernameRecipe().equalsIgnoreCase(user.getUsername())))
            throw new RecipeNotFoundException("Recipe not found");
        user.getRecipeList().remove(recipe);
        recipeRepository.deleteById(recipeId);
        userRepository.save(user);
    }
}