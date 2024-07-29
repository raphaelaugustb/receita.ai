package com.leah.receitas.ai.dto.user;

import com.leah.receitas.ai.entity.Recipe;

import java.util.List;

public record UserResponse(String username, String email, String password, List<Recipe> recipeList,  List<Recipe> savedRecipeList) {
}
