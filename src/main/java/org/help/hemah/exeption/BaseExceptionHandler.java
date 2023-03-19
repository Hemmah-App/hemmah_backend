package org.help.hemah.exeption;

import lombok.extern.slf4j.Slf4j;
import org.help.hemah.helper.res_model.ResponseModel;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@RestControllerAdvice
@Slf4j
public class BaseExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(
                ResponseModel.builder()
                        .statusCode(BAD_REQUEST.value())
                        .status(BAD_REQUEST)
                        .message("INPUT DATA IS NOT VALID.")
                        .reason(
                                ex.getFieldErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .collect(Collectors.joining(",")))
                        .data(
                                Map.of("errors",
                                        ex.getBindingResult()
                                                .getFieldErrors()
                                                .stream()
                                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                                .collect(Collectors.toList())))
                        .build());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(
                ResponseModel.builder()
                        .statusCode(BAD_REQUEST.value())
                        .status(BAD_REQUEST)
                        .message("Exception.")
                        .reason(ex.getMessage())
                        .build());
    }

}
