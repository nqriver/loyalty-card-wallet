package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.nqriver.cardwallet.card.application.service.OperationTerminatedWithFailureException;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.ApiErrorResponse;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.exception.ResourceNotFoundException;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(OperationTerminatedWithFailureException.class)
    public ResponseEntity<?> handleOperationFailureException(OperationTerminatedWithFailureException exception) {
        final ApiErrorResponse errorResponse = ApiErrorResponse.withSimpleMessage(HttpStatus.BAD_REQUEST,
                exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException exception) {
        final ApiErrorResponse errorResponse = ApiErrorResponse.withSimpleMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception) {
        final ApiErrorResponse errorResponse = ApiErrorResponse.withSimpleMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        final Map<String, String> errorMap = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .collect(Collectors.toMap(error -> ((FieldError) error).getField(),
                        objectError -> Objects.isNull(objectError.getDefaultMessage()) ? "" : objectError.getDefaultMessage()));

        final ApiErrorResponse errorResponse =
                ApiErrorResponse.withMapOfErrors(HttpStatus.BAD_REQUEST, exception.getMessage(), errorMap);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataViolations(DataIntegrityViolationException exception) {
        String rootMsg = exception.getRootCause().getMessage();
        final ApiErrorResponse errorResponse = ApiErrorResponse.withSimpleMessage(HttpStatus.CONFLICT, rootMsg);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAnyException(Exception exception) {
        final ApiErrorResponse errorResponse = ApiErrorResponse
                .withSimpleMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
