package com.codebyisaac.mapi.exception;

import com.codebyisaac.mapi.dto.APIResponse;
import com.codebyisaac.mapi.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<APIResponse<Void>> handleNotFound (ProductNotFoundException ex) {
    return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<APIResponse<Void>> handleUserNotFound(UserNotFoundException ex) {
    return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(InsufficientStockException.class)
  public ResponseEntity<APIResponse<Void>> handleInsufficientStock(InsufficientStockException ex) {
    return buildError(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<APIResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
    List<ErrorDTO> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> new ErrorDTO(fe.getField(), fe.getDefaultMessage()))
            .toList();
    APIResponse<Void> response = APIResponse.<Void>builder()
            .status("FAILURE")
            .errors(errors)
            .build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<APIResponse<Void>> buildError(HttpStatus status, String message){
    APIResponse<Void> response = APIResponse.<Void>builder().status("FAILURE")
            .errors(List.of(new ErrorDTO("error", message)))
            .build();
    return new ResponseEntity<>(response, status);
  }

}