package com.leah.receitas.ai.exception;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(String message) {
        super(message);
    }
}
