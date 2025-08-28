package io.github.hyungjun.backend.exception;

public class EmailAlreadyExistsException extends DuplicateResourceException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
