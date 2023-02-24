package com.eldentech.user.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;


public class ErrorOnSavingUser extends ResponseStatusException {
    public ErrorOnSavingUser() {
        super(HttpStatusCode.valueOf(500), "Unexpected error occurred while adding user");
    }
}
