package com.example.fakecommerce.exceptions;

public class ResourceDeletionException extends RuntimeException {
    public ResourceDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
