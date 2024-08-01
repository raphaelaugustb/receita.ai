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
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    //Buscar uma recipe no repositório público
    @GetMapping({"/recipe"})
    public ResponseEntity<List<RecipeResponse>> getRecipePublic(@RequestParam(name = "name") String recipeName) {
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.getRecipesByNamePublic(recipeName));
    }

    //Buscar uma recipe no do usuário
    @GetMapping("{userId}/recipe")
    public ResponseEntity<List<RecipeResponse>> getUserRecipeOnList(@PathVariable UUID userId, @RequestParam(name = "name") String recipeName) {
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.getUserRecipesByName(userId, recipeName));
    }

    //Criar uma nova recipe e endereçar ela um usuário e alimentar a API
    @PostMapping("{userId}/recipe")
    public ResponseEntity<RecipeResponse> createRecipe(@PathVariable UUID userId, @RequestBody RecipeRequest recipeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.createRecipe(recipeRequest, userId));
    }

    //Remover uma recipe da lista de salvos
    @DeleteMapping("{userId}/recipe/save/{recipeId}")
    public ResponseEntity<String> removeSavedRecipe(@PathVariable UUID userId, @PathVariable long recipeId) {
        recipeService.removeRecipeSaved(userId, recipeId);
        return ResponseEntity.status(HttpStatus.OK).body("Recipe successfully removed");
    }

    //Salvar um link simbólico pelo Id na lista de recipes salvas
    @PostMapping("{userId}/recipe/save/{recipeId}")
    public ResponseEntity<String> saveRecipeFromAnotherUser(@PathVariable UUID userId, @PathVariable long recipeId) {
        recipeService.saveRecipeFromUsers(userId, recipeId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Recipe successfully saved");
    }

    //Acessar a lista de links simbólicos e buscar no repositório pelo id as recipes
    @GetMapping("{userId}/recipe/save")
    public ResponseEntity<List<RecipeResponse>> getUserSavedRecipes(@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.listSavedRecipes(userId));
    }

    //Deletar as receitas do usuário
    @DeleteMapping("{userId}/recipe/{recipeId}")
    public ResponseEntity<String> deleteUserRecipe(@PathVariable UUID userId, @PathVariable Long recipeId) {
        recipeService.deleteRecipe(userId, recipeId);
        return ResponseEntity.status(HttpStatus.OK).body("Recipe successfully removed");
    }

    //Alterar alguma receita do usuário
    @PutMapping("{userId}/recipe/{recipeId}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable UUID userId, @PathVariable Long recipeId, @RequestBody RecipeRequest recipeRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(recipeService.updateRecipeInfo(userId, recipeId, recipeRequest));
    }
}
