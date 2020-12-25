package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.Serializable;

@ControllerAdvice
public class DemoExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private class Result implements Serializable {
        private String code;
        private String message;

        public String getCode() {
            return code;
        }
        public String getMessage() {
            return message;
        }

        private Result(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    @ExceptionHandler(DemoException.class)
    @ResponseBody
    public ResponseEntity<Result> handleDemoException(DemoException ex) {
        logger.error("Demo Exception", ex.getMessage(), ex);
        Result result = new Result(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Result> handleOtherError(Exception ex) {
        logger.error("Unknown Exception", ex.getMessage(), ex);
        Result result = new Result("Demo-50000", ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
