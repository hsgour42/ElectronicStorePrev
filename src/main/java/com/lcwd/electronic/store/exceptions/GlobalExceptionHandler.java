package com.lcwd.electronic.store.exceptions;

import com.lcwd.electronic.store.dtos.dtoResponseHelper.ApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


    //handle resource not found exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException exception){
            ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                    .message(exception.getMessage())
                    .success(false)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
            return new ResponseEntity(apiResponseMessage, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(RuntimeException exception){
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message(exception.getMessage())
                .success(false)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity(apiResponseMessage, HttpStatus.NOT_FOUND);

    }

    //handle Method Argument Not Valid Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String , Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            Map<String , Object> response = new HashMap<>();
            allErrors.stream().map(err -> {
                 String message = err.getDefaultMessage();
                 String field = ((FieldError)err).getField();
                 response.put(field , message);
                 return response;
            }).collect(Collectors.toList());
            return new ResponseEntity(response , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponseMessage> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex){
          ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                  .message(ex.getMessage())
                  .success(false)
                  .httpStatus(HttpStatus.IM_USED)
                  .build();
          return new ResponseEntity(apiResponseMessage , HttpStatus.IM_USED);


    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ApiResponseMessage> handleClassCastException(ClassCastException ex){
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .httpStatus(HttpStatus.IM_USED)
                .build();
        return new ResponseEntity(apiResponseMessage , HttpStatus.IM_USED);
    }

    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> handleClassCastException(BadApiRequestException ex){
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity(apiResponseMessage , HttpStatus.BAD_REQUEST);
    }



}
