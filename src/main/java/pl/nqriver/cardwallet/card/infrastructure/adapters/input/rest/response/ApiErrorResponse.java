package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class ApiErrorResponse {

    private final LocalDateTime timestamp;

    private final HttpStatus status;

    private final String message;

    private final List<String> errors;

    public ApiErrorResponse(final HttpStatus status, final String message, final List<String> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiErrorResponse(final HttpStatus status, final String message, final Map<String, String> errors) {
        this(status, message, convertMapOfErrorsToList(errors));
    }

    public ApiErrorResponse(final HttpStatus status, final String message, final String error) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errors = List.of(error);
    }

    private static List<String> convertMapOfErrorsToList(Map<String, String> errors) {
        return errors.entrySet()
                .stream()
                .map(e -> String.format("%s : %s", e.getKey(), e.getValue()))
                .toList();
    }

}
