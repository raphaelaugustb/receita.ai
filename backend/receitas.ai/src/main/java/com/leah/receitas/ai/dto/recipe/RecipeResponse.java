package com.leah.receitas.ai.dto.recipe;

import java.util.List;

public record RecipeResponse(String recipeName, List<String> ingredients, List<String> instructions, Boolean isPrivate, String username) {
}
