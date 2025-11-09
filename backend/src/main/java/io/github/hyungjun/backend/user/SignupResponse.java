package io.github.hyungjun.backend.user;

public record SignupResponse(Long memberId, String email, String password, String nickname) {
    public SignupResponse(Member member) {
        this(member.getId(), member.getEmail(), member.getPassword(), member.getNickname());
    }
}
