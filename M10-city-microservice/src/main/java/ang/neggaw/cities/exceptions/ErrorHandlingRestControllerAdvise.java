package ang.neggaw.cities.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ErrorHandlingRestControllerAdvise {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<Violation> onConstraintViolationException(ConstraintViolationException e) {
        ValidationErrorResponse.violations = new ArrayList<>();
        e.getConstraintViolations().forEach(cv -> {
            ValidationErrorResponse.violations.add(new Violation(cv.getPropertyPath().toString(), cv.getMessage()));
        });
        return ValidationErrorResponse.violations;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<Violation> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ValidationErrorResponse.violations = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            ValidationErrorResponse.violations.add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        });
        return ValidationErrorResponse.violations;
    }


    @Data
    @AllArgsConstructor
    public static class Violation {
        private String fieldName;
        private String errorMessage;
    }

    @Data
    public static class ValidationErrorResponse {
        private static List<Violation> violations;
    }
}
