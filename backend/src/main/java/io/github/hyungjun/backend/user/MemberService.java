package io.github.hyungjun.backend.user;

import io.github.hyungjun.backend.exception.EmailAlreadyExistsException;
import io.github.hyungjun.backend.exception.NicknameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member signup(Member member) {
        validateEmailNotDuplicated(member.getEmail());
        validateNickNameNotDuplicated(member.getNickname());

        String encodedPassword = passwordEncoder.encode(member.getPassword());
        Member signupMember = Member.signup(member.getEmail(), encodedPassword, member.getNickname());
        return memberRepository.save(signupMember);
    }

    private void validateNickNameNotDuplicated(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new NicknameAlreadyExistsException("Nickname already exists");
        }
    }

    private void validateEmailNotDuplicated(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }
}
