package com.web.base.user.controller;

import com.web.base.common.base.BusinessException;
import com.web.base.common.web.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author donghonghua
 * @date 2018/8/24
 */
@RestControllerAdvice
public class ExceptionController {

    private final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(BusinessException.class)
    public WebResponse<?> catchException(BusinessException e) {
        logger.info(e.getMessage());
        return WebResponse.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<WebResponse<?>> httpException(HttpClientErrorException e) {
        logger.info(e.getMessage());
        WebResponse<?> response = WebResponse.fail(e.getStatusCode().value(), e.getMessage());
        return ResponseEntity.status(e.getStatusCode().value()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public WebResponse<?> runtimeException(RuntimeException e) {
        logger.info(e.getMessage());
        return WebResponse.fail(-1, e.getMessage());
    }

}
