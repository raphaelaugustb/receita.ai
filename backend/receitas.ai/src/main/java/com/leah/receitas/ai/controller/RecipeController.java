package com.leah.receitas.ai.controller;

import com.leah.receitas.ai.dto.recipe.RecipeRequest;
import com.leah.receitas.ai.dto.recipe.RecipeResponse;
import com.leah.receitas.ai.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class RecipeController {

    private final RecipeService recipeService;

    //TODO
    // Melhorar qualidades da rotas
    // Separar serviços para listar salvos e lista do usuário
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"/recipe"})
    public ResponseEntity<List<RecipeResponse>> getRecipePublic(@RequestParam(name = "name") String recipeName) {
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.getRecipesByNamePublic(recipeName));
    }

    @GetMapping("{userId}/recipe")
    public ResponseEntity<List<RecipeResponse>> getUserRecipeOnList(@PathVariable UUID userId, @RequestParam(name = "name") String recipeName) {
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.getUserRecipesByName(userId, recipeName));
    }

    @PostMapping("{userId}/recipe")
    public ResponseEntity<RecipeResponse> createRecipe(@PathVariable UUID userId, @RequestBody RecipeRequest recipeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.createRecipe(recipeRequest, userId));
    }

    @DeleteMapping("{userId}/recipe/save/{recipeId}")
    public ResponseEntity<String> removeSavedRecipe(@PathVariable UUID userId, @PathVariable long recipeId) {
        recipeService.removeRecipeSaved(userId, recipeId);
        return ResponseEntity.status(HttpStatus.OK).body("Recipe successfully removed");
    }

    @PostMapping("{userId}/recipe/save/{recipeId}")
    public ResponseEntity<String> saveRecipeFromAnotherUser(@PathVariable UUID userId, @PathVariable long recipeId) {
        recipeService.saveRecipeFromUsers(userId, recipeId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Recipe successfully saved");
    }

    @GetMapping("{userId}/recipe/save")
    public ResponseEntity<List<RecipeResponse>> getUserSavedRecipes(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.listSavedRecipes(userId));
    }

    @DeleteMapping("{userId}/recipe/{recipeId}")
    public ResponseEntity<String> deleteUserRecipe(@PathVariable UUID userId, @PathVariable Long recipeId) {
        recipeService.deleteRecipe(userId, recipeId);
        return ResponseEntity.status(HttpStatus.OK).body("Recipe successfully removed");
    }

    @PutMapping("{userId}/recipe/{recipeId}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable UUID userId, @PathVariable Long recipeId, @RequestBody RecipeRequest recipeRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(recipeService.updateRecipeInfo(userId, recipeId, recipeRequest));
    }
}
