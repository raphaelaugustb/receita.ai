package com.leah.receitas.ai.controller;

import com.leah.receitas.ai.dto.recipe.RecipeRequest;
import com.leah.receitas.ai.dto.recipe.RecipeResponse;
import com.leah.receitas.ai.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("{userId}/recipe")
    public ResponseEntity<RecipeResponse> createRecipe(@PathVariable UUID userId, @RequestBody RecipeRequest recipeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.createRecipe(recipeRequest, userId));
    }

    @DeleteMapping("{userId}/recipe/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@PathVariable UUID userId, @PathVariable Long recipeId) {
        recipeService.deleteRecipe(userId,recipeId);
        return ResponseEntity.status(HttpStatus.OK).body("Recipe successfully deleted");
    }
}
