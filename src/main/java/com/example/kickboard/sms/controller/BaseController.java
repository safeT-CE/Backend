// 전역적 예외 처리를 위한 클래스

package com.example.kickboard.sms.controller;

import com.example.kickboard.sms.exception.CustomExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = CustomExceptions.Exception.class)
    public ResponseEntity handleCustomException(CustomExceptions.Exception e) {
        return handleApiException(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleException(Exception e) {
        return handleApiException(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ResponseEntity handleApiException(Exception e, HttpStatus status) {
        Map<String, String> res = new HashMap<>();
        res.put("statusCode", "error");
        res.put("responseMessage", e.getMessage() != null ? e.getMessage() : "An error occurred");
        logger.error("error:{}", e.getMessage());
        return new ResponseEntity<>(res, status);
    }
}