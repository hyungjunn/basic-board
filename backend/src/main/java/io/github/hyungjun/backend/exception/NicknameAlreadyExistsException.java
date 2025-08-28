package io.github.hyungjun.backend.exception;

public class NicknameAlreadyExistsException extends DuplicateResourceException {
    public NicknameAlreadyExistsException(String message) {
        super(message);
    }
}
