package com.leah.receitas.ai.dto.recipe;

import java.util.List;

public record RecipeRequest(String recipeName, List<String> ingredients, List<String> instructions, Boolean isPrivate) {
}
