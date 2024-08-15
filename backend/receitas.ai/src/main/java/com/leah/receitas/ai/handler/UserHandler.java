package com.leah.receitas.ai.handler;

import com.leah.receitas.ai.dto.user.UserPublicInfo;
import com.leah.receitas.ai.dto.user.UserRequest;
import com.leah.receitas.ai.dto.user.UserResponse;
import com.leah.receitas.ai.exception.UserNotFoundException;
import com.leah.receitas.ai.models.ExceptionHandlerModel;
import com.leah.receitas.ai.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class UserHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionHandlerModel> handleUserNotFoundException(UserNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionHandlerModel(HttpStatus.NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND.value()));
  }

}
