package io.github.hyungjun.backend.user;

public record UserSignupResponse(Long userId, String email, String password, String nickname) {
    public UserSignupResponse(User user) {
        this(user.getId(), user.getEmail(), user.getPassword(), user.getNickname());
    }
}
