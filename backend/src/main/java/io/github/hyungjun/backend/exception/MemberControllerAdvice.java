package io.github.hyungjun.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicate(EmailAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("EMAIL_DUPLICATE", e.getMessage()));
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleNicknameDuplicate(NicknameAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("NICKNAME_DUPLICATE", e.getMessage()));
    }
}
