package br.ufpb.dcx.dsc.repositorios.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(
            NotFoundException ex
    ){
        return buildErroResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex
    ){
        return buildErroResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex){
        List<String> errorsList = ex.getConstraintViolations().stream()
                .map(error -> error.getPropertyPath() + ": " + error.getMessage())
                .toList();
        return ResponseEntity.unprocessableEntity().body(buildErroResponse(errorsList, HttpStatus.BAD_REQUEST));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return buildErroResponse(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleGenericException(
            Exception ex
    ){
        return buildErroResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildErroResponse(
        Exception ex,
        HttpStatus status
    ){
        ErrorResponse errorResponse = new ErrorResponse
                .Builder()
                .localDateTime(LocalDateTime.now())
                .code(status.value())
                .status(status.name())
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
    private ResponseEntity<Object> buildErroResponse(
            List<String> erros,
            HttpStatus status
    ){
        ErrorResponse errorResponse = new ErrorResponse
                .Builder()
                .localDateTime(LocalDateTime.now())
                .code(status.value())
                .status(status.name())
                .errors(erros)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
