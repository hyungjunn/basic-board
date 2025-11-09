package io.github.hyungjun.backend.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/api/v1/members")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        Member member = request.toMember();
        Member savedMember = memberService.signup(member);
        return ResponseEntity.ok(new SignupResponse(savedMember));
    }
}
