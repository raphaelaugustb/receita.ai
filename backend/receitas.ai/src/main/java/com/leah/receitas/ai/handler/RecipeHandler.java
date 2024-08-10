package com.leah.receitas.ai.handler;

import com.leah.receitas.ai.dto.recipe.RecipeRequest;
import com.leah.receitas.ai.dto.recipe.RecipeResponse;
import com.leah.receitas.ai.exception.RecipeNotFoundException;
import com.leah.receitas.ai.exception.UserNotFoundException;
import com.leah.receitas.ai.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecipeHandler {
    RecipeService recipeService;

    public RecipeHandler(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public ResponseEntity<List<RecipeResponse>> queryRecipesByNameHandler(String recipeName) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.queryRecipesByName(recipeName));
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public ResponseEntity<List<RecipeResponse>> queryRecipeOnUserListHandler(String username, String recipeName) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.queryRecipesOnUserList(username, recipeName));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public ResponseEntity<RecipeResponse> createNewRecipeHandler(UUID userId, RecipeRequest recipeRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.createRecipe(recipeRequest, userId));
        } catch (RuntimeException e) {
            if (e.getClass() == UserNotFoundException.class) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
    }

    public ResponseEntity<Void> deleteSavedRecipeHandler(UUID userId, long recipeId) {
        try {
            recipeService.deleteSavedRecipe(userId, recipeId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            if (e.getClass() == UserNotFoundException.class || e.getClass() == RecipeNotFoundException.class) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
    }
    public ResponseEntity<RecipeResponse> updateRecipeInfoHandler(UUID userId, long recipeId, RecipeRequest recipeRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(recipeService.updateRecipeInfo(userId, recipeId, recipeRequest));
        }   catch (RuntimeException e) {
        if (e.getClass() == UserNotFoundException.class || e.getClass() == RecipeNotFoundException.class) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    }
    public ResponseEntity<Void> deleteRecipeFromUserList(UUID userId, long recipeId) {
        try {
            recipeService.deleteRecipe(userId, recipeId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            if (e.getClass() == UserNotFoundException.class || e.getClass() == RecipeNotFoundException.class) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
    }
    public ResponseEntity<String> saveRecipeFromUserHandler(UUID userId, long recipeId) {
        try {
            recipeService.saveRecipeFromAnotherUser(userId, recipeId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Recipe Successfully saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe not found");
        }

    }
    public ResponseEntity<List<RecipeResponse>> getSavedRecipesListHandler( UUID userId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body( recipeService.listSavedRecipes(userId));
        } catch (RecipeNotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        }
    }
