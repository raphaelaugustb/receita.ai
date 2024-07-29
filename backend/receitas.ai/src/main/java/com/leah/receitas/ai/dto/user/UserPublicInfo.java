package com.leah.receitas.ai.dto.user;

import com.leah.receitas.ai.entity.Recipe;

import java.util.List;

public record UserPublicInfo(String username, List<Recipe> userPublicRecipeList) {
}
