package br.com.sccon.rh.config;

import br.com.sccon.rh.exception.ConflictException;
import br.com.sccon.rh.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String ERROR_ALIAS = "errors";

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        return handleExceptionInternal(ex, getErrors(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException ex, final WebRequest request) {
        return handleExceptionInternal(ex, getErrors(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(final NotFoundException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(final ConflictException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handleGenException(final ResponseStatusException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ex.getCause(), new HttpHeaders(), ex.getStatus(), request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGenException(final Exception ex, final WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, @Nullable final Object body, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logException(ex, body, status, request.toString());
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Map<String, Object> getErrors(final ConstraintViolationException ex) {
        final List<String> errors = Optional.ofNullable(ex.getConstraintViolations())
            .orElse(Collections.emptySet())
            .stream()
            .filter(cns -> Objects.nonNull(cns.getPropertyPath()))
            .map(this::createConstraintError)
            .collect(Collectors.toList());
        return Map.of(ERROR_ALIAS, ObjectUtils.firstNonNull(errors, ex.getMessage()));
    }

    private Map<String, Object> getErrors(final MethodArgumentNotValidException ex) {
        final List<String> errors = Optional.ofNullable(ex.getBindingResult())
            .map(BindingResult::getFieldErrors)
            .orElse(Collections.emptyList())
            .stream()
            .map(this::createFieldError)
            .collect(Collectors.toList());
        return Map.of(ERROR_ALIAS, ObjectUtils.firstNonNull(errors, ex.getMessage()));
    }

    private String createFieldError(final FieldError fieldError) {
        return String.format("%s %s", StringUtils.capitalize(fieldError.getField()), this.messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()));
    }

    private String createConstraintError(final ConstraintViolation<?> constraintViolation) {
        return String.format("%s %s", StringUtils.capitalize(constraintViolation.getPropertyPath().toString()), constraintViolation.getMessage());
    }

    private void logException(final Exception ex, final Object body, final HttpStatus httpStatus, final String url) {
        final String message = String.format("Request: %s, error: %s", url, ObjectUtils.firstNonNull(body, ex.getMessage()));
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(httpStatus)) {
            logger.error(message, ex);
        } else {
            logger.error(message);
        }
    }

}
