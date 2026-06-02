package com.strivolabs.strivolabsassessmentjava.common.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.strivolabs.strivolabsassessmentjava.common.constants.Http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // validation errors - @Valid on request bodies
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        logger.info("Validation errors: {}", errors);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "One or more validation errors occurred");
        problem.setTitle("Validation Error");
        problem.setType(URI.create(Http.MDN_BASE_URL + HttpStatus.BAD_REQUEST.value()));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(
            ApiException ex,
            HttpServletRequest request) {

        logger.info("{}: {}", ex.getTitle(), ex.getError());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                ex.getStatusCode(),
                ex.getMessage());
        problem.setTitle(ex.getTitle());
        problem.setType(URI.create(Http.MDN_BASE_URL + ex.getStatusCode().value()));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("error", ex.getError());

        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(
            Exception ex,
            HttpServletRequest request) {

        logger.error("Unhandled exception occurred while processing request", ex);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred");
        problem.setTitle("Server Error");
        problem.setType(URI.create(Http.MDN_BASE_URL + HttpStatus.INTERNAL_SERVER_ERROR.value()));
        problem.setInstance(URI.create(request.getRequestURI()));

        return problem;
    }
}