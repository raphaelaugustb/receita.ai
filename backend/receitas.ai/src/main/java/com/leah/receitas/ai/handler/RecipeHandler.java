package com.leah.receitas.ai.handler;

import com.leah.receitas.ai.exception.RecipeNotFoundException;
import com.leah.receitas.ai.models.ExceptionHandlerModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RecipeHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ExceptionHandlerModel> handleRecipeNotFoundException(RecipeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionHandlerModel(HttpStatus.NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

}
