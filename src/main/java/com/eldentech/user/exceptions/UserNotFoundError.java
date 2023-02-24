package com.eldentech.user.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;


public class UserNotFoundError extends ResponseStatusException {
    public UserNotFoundError(String id) {
        super(HttpStatusCode.valueOf(404), "User can not be found with id:%s".formatted(id));
    }
}
