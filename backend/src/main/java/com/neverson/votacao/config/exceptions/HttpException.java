package com.neverson.votacao.config.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {
    @Getter
    private HttpStatus httpStatus;

    public HttpException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public static HttpException badRequest(String message) {
        return new HttpException(HttpStatus.BAD_REQUEST, message);
    }
    public static HttpException notFound(String message) {
        return new HttpException(HttpStatus.NOT_FOUND, message);
    }

    public static HttpException internalServerError(String message) {
        return new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static HttpException conflict(String message) {
        return new HttpException(HttpStatus.CONFLICT, message);
    }
}
