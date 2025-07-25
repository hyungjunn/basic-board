package io.github.hyungjun.backend.user;

public record UserSignupRequest(String email, String password, String nickname) {
    public User toUser() {
        return new User(email, password, nickname);
    }
}
