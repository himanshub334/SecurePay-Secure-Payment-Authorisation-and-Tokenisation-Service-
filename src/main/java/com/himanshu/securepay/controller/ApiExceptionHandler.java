package com.himanshu.securepay.controller;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import org.springframework.web.bind.MethodArgumentNotValidException; import java.util.Map;
@RestControllerAdvice public class ApiExceptionHandler {
 @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<Map<String,String>> badRequest(IllegalArgumentException ex){return ResponseEntity.badRequest().body(Map.of("error","BAD_REQUEST","message",ex.getMessage()));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,String>> validation(MethodArgumentNotValidException ex){return ResponseEntity.badRequest().body(Map.of("error","VALIDATION_ERROR","message","Request validation failed"));}
}
