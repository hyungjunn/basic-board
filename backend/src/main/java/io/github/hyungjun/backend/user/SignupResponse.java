package io.github.hyungjun.backend.user;

public record SignupResponse(Long memberId, String email, String nickname) {
    public SignupResponse(Member member) {
        this(member.getId(), member.getEmail(), member.getNickname());
    }
}
