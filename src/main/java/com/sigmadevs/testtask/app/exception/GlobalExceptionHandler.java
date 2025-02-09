package com.sigmadevs.testtask.app.exception;

import com.sigmadevs.testtask.security.exception.UserNotFoundException;
import com.sigmadevs.testtask.security.exception.UsernameAlreadyExistsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException e) {

        String defaultMessage = e.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Message validation error!");

        return new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.value(), defaultMessage);
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CommentNotFoundException.class)
    public ApiError handleCommentNotFoundException(CommentNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OptionNotFoundException.class)
    public ApiError handleOptionNotFoundException(OptionNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(QuestNotFoundException.class)
    public ApiError handleQuestNotFoundException(QuestNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ApiError handleTaskNotFoundException(TaskNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ApiError handleUserNotFoundException(UserNotFoundException e){
        return new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ApiError handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e){
        return new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            return new ApiError(HttpStatus.CONFLICT.value(), "User already exists");
        }
        return new ApiError(HttpStatus.BAD_REQUEST.value(), "Bad request");
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ApiError handleThrowable(Throwable e) {

        log.error("An unexpected error occurred: {}", e.getMessage());

        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + e.getMessage());
    }

}
