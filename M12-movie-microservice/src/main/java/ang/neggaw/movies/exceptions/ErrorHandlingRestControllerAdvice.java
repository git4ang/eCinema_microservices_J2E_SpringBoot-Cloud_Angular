package ang.neggaw.movies.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ResponseBody
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ErrorHandlingRestControllerAdvice {


    @ExceptionHandler(ConstraintViolationException.class)
    public List<Violation> onConstraintViolationException(ConstraintViolationException e) {
        ValidationResponseError.violations = new ArrayList<>();
        e.getConstraintViolations().forEach(cv -> {
            ValidationResponseError.violations.add(new Violation(cv.getPropertyPath().toString(), cv.getMessage()));
        });
        return ValidationResponseError.violations;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<Violation> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ValidationResponseError.violations = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            ValidationResponseError.violations.add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        });
        return ValidationResponseError.violations;
    }


    @Data
    @AllArgsConstructor
    public static class Violation {
        private String fieldName;
        private String errorMessage;
    }

    @Data
    public static class ValidationResponseError {
        private static List<Violation> violations;
    }
}