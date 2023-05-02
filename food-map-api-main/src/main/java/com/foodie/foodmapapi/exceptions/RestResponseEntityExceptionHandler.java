package com.foodie.foodmapapi.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ ServiceException.class })
    public ResponseEntity<Object> handleConflict(ServiceException e) {
        HttpStatus status = HttpStatus.I_AM_A_TEAPOT;
        if (e instanceof InvalidParameterException) {
            status = HttpStatus.BAD_REQUEST;
        }
        else if (e instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }
        else if (e instanceof MethodNotImplementedException) {
            status = HttpStatus.NOT_IMPLEMENTED;
        }
        else if (e instanceof ApiRequestException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(e.getMessage(), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return this.handleWrappedException(e);
    }

    private ResponseEntity<Object> handleWrappedException(Exception e) {
        Throwable t = e;
        while (t != null) {
            if (t instanceof ServiceException) {
                return this.handleConflict((ServiceException) t);
            }
            t = t.getCause();
        }

        if (e != null) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
