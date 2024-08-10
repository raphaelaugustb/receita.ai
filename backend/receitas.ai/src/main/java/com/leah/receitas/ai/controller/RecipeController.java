package com.leah.receitas.ai.controller;

import com.leah.receitas.ai.dto.recipe.RecipeRequest;
import com.leah.receitas.ai.dto.recipe.RecipeResponse;
import com.leah.receitas.ai.handler.RecipeHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class RecipeController {

    RecipeHandler recipeHandler;

    public RecipeController(RecipeHandler recipeHandler) {
        this.recipeHandler = recipeHandler;
    }

    //Buscar uma recipe no repositório público
    @GetMapping({"/recipe"})
    public ResponseEntity<List<RecipeResponse>> getPublicRecipesByName(@RequestParam(name = "query") String recipeName) {
        return recipeHandler.queryRecipesByNameHandler(recipeName);
    }

    //Buscar uma recipe na lista do usuário
    @GetMapping("{username}/recipe")
    public ResponseEntity<List<RecipeResponse>> getUserRecipeOnListByName(@PathVariable String username, @RequestParam(name = "name") String recipeName) {
        return recipeHandler.queryRecipeOnUserListHandler(username, recipeName);
    }

    //Criar uma nova recipe e endereçar ela um usuário e alimentar a API
    @PostMapping("{userId}/recipe")
    public ResponseEntity<RecipeResponse> createRecipe(@PathVariable UUID userId, @RequestBody RecipeRequest recipeRequest) {
        return recipeHandler.createNewRecipeHandler(userId, recipeRequest);
    }

    //Remover uma recipe da lista de salvos
    @DeleteMapping("{userId}/recipe/save/{recipeId}")
    public ResponseEntity<Void> removeSavedRecipe(@PathVariable UUID userId, @PathVariable long recipeId) {
        return recipeHandler.deleteSavedRecipeHandler(userId, recipeId);
    }

    //Salvar um link simbólico pelo Id na lista de recipes salvas
    @PostMapping("{userId}/recipe/save/{recipeId}")
    public ResponseEntity<String> saveRecipeFromAnotherUser(@PathVariable UUID userId, @PathVariable long recipeId) {
        return recipeHandler.saveRecipeFromUserHandler(userId, recipeId);
    }

    //Acessar a lista de links simbólicos e buscar no repositório pelo id as recipes
    @GetMapping("{userId}/recipe/save")
    public ResponseEntity<List<RecipeResponse>> getUserSavedRecipes(@PathVariable UUID userId) {
        return recipeHandler.getSavedRecipesListHandler(userId);
    }

    //Deletar as receitas do usuário
    @DeleteMapping("{userId}/recipe/{recipeId}")
    public ResponseEntity<Void> deleteUserRecipe(@PathVariable UUID userId, @PathVariable Long recipeId) {
        return recipeHandler.deleteRecipeFromUserList(userId, recipeId);
    }

    //Alterar alguma receita do usuário
    @PutMapping("{userId}/recipe/{recipeId}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable UUID userId, @PathVariable Long recipeId, @RequestBody RecipeRequest recipeRequest) {
        return recipeHandler.updateRecipeInfoHandler(userId,recipeId,recipeRequest);
    }
}
