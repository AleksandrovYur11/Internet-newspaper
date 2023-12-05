package ru.aleksandrov.backendinternetnewspaper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.aleksandrov.backendinternetnewspaper.security.exception.TokenRefreshException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
//        Map<String, String> errorResponse = new HashMap<>();
//        List<String> validationErrors = new ArrayList<>();
//        exception.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String message = error.getDefaultMessage();
//            errorResponse.put(fieldName, message);
//        });
//        ErrorResponse errorResponse = new ErrorResponse("Validation Failed", validationErrors);
        Map<String, String> errorResponse = exception.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        error -> error.getDefaultMessage()
                ));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> errorResponse = exception.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    public ResponseEntity<Object> handlerTokenRefreshException(TokenRefreshException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("refreshToken", exception.getMessage());
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityFoundException(EntityNotFoundException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", exception.getMessage());
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage()); // Добавляем сообщение об ошибке

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalAccessException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage()); // Добавляем сообщение об ошибке

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
//        Map<String, String> errors = new HashMap<>();
//        errors.put("email", "User with this email already exists");
//        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
//    }

//    private ResponseEntity<Object> getExceptionResponseEntity(final HttpStatus status, WebRequest request, List<String> errors) {
//        final Map<String, Object> body = new LinkedHashMap<>();
//        final String errorsMessage = CollectionUtils.isNotEmpty(errors) ? errors.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining(",")):status.getReasonPhrase();
//        final String path = request.getDescription(false);
//        body.put("TIMESTAMP", Instant.now());
//        body.put("STATUS", status.value());
//        body.put("ERRORS", errorsMessage);
//        body.put("PATH", path);
//        body.put("MESSAGE", status.getReasonPhrase());
//        return new ResponseEntity<>(body, status);
//    }


    //-------------------------------------------
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//
//            String fieldName = ((FieldError) error).getField();
//            String message = error.getDefaultMessage();
//            errors.put(fieldName, message);
//        });
//        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ErrorSignupResponse handleConstraintViolationException(ConstraintViolationException ex) {
//        List<String> details = new ArrayList<String>();
//        details.add(ex.getMessage());
//
//        ErrorSignupResponse err = new ErrorSignupResponse();
//        return err;
//    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public void handleConstraintViolation(
//            ConstraintViolationException ex) {
//        List<String> errors = new ArrayList<String>();
//        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
//            errors.add(violation.getRootBeanClass().getName() + " " +
//                    violation.getPropertyPath() + ": " + violation.getMessage());
//        }
//
//        errors.stream().forEach(error -> System.out.println(error));
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleMethodArgumentNotValid(EntityNotFoundException ex) {
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getMessage().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage()));
//        return errors;
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ErrorResponse handleEntityFoundException(EntityNotFoundException ex) {
//        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(EmptyResultDataAccessException.class)
//    public ErrorResponse handleEntityFoundException(EmptyResultDataAccessException ex) {
//        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
//    }
//
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(TokenRefreshException.class)
//    public ErrorResponse handleEntityFoundException(TokenRefreshException ex) {
//        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
//    }

//    @ExceptionHandler(value = TokenRefreshException.class)
//    public Map<String, String> handlerTokenRefreshException(TokenRefreshException ex) {
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage()));
//    }

//    @ExceptionHandler(value = CustomerAlreadyExistsException.class)
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public ErrorResponse
//    handleCustomerAlreadyExistsException(
//            CustomerAlreadyExistsException ex) {
//        return new ErrorResponse(HttpStatus.CONFLICT.value(),
//                ex.getMessage());
//    }

}
