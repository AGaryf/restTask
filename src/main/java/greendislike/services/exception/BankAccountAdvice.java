package greendislike.services.exception;

import greendislike.services.exception.type.BankAccountDuplicateException;
import greendislike.services.exception.type.BankAccountNotEnoughException;
import greendislike.services.exception.type.BankAccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
public class BankAccountAdvice {

    @ResponseBody
    @ExceptionHandler(BankAccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse bankAccountNotFoundHandler(BankAccountNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(BankAccountDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse bankAccountDuplicateHandler(BankAccountDuplicateException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(BankAccountNotEnoughException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse bankAccountNotEnoughHandler(BankAccountNotEnoughException ex) {
        return new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.toString(), ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException .class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse bankAccountNotValidHandler(MethodArgumentNotValidException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getBindingResult().getFieldError().getField() + ": " +
                ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage());
        }
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), strBuilder.toString());
    }
}
