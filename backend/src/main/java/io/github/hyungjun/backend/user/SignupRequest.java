package io.github.hyungjun.backend.user;

public record SignupRequest(String email, String password, String nickname) {
    public Member toMember() {
        return new Member(email, password, nickname);
    }
}
