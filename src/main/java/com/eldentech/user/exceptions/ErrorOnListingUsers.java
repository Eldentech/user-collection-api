package com.eldentech.user.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;


public class ErrorOnListingUsers extends ResponseStatusException {
    public ErrorOnListingUsers() {
        super(HttpStatusCode.valueOf(500), "Unexpected error occurred while listing users");
    }
}
