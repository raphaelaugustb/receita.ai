package com.leah.receitas.ai.repository;

import com.leah.receitas.ai.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
