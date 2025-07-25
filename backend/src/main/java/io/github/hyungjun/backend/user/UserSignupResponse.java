package io.github.hyungjun.backend.user;

public record UserSignupResponse(String email, String password, String nickname) {
    public UserSignupResponse(User user) {
        this(user.getEmail(), user.getPassword(), user.getNickname());
    }
}
