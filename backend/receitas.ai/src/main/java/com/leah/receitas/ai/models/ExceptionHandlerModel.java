package com.leah.receitas.ai.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionHandlerModel {

    private HttpStatus status;
    private String message;
    private int httpStatusCode;
}
