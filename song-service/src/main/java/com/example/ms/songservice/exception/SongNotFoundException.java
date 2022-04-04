package com.example.ms.songservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SongNotFoundException extends RuntimeException {
    public SongNotFoundException() {
        super();
    }

    public SongNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SongNotFoundException(String message) {
        super(message);
    }

    public SongNotFoundException(Throwable cause) {
        super(cause);
    }
}
