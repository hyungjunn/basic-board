package io.github.hyungjun.backend.user;

import io.github.hyungjun.backend.exception.EmailAlreadyExistsException;
import io.github.hyungjun.backend.exception.NicknameAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        String rawPassword = "password1234";
        Member member = new Member("example@google.com", rawPassword, "nickname");

        Member signupMember = memberService.signup(member);

        assertThat(signupMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(signupMember.getNickname()).isEqualTo(member.getNickname());
        assertThat(signupMember.getPassword()).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, signupMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void duplicateEmail_throwsException1() {
        Member member1 = new Member("example@google.com", "password1234", "nickname1");
        memberService.signup(member1);
        Member member2 = new Member("example@google.com", "password5678", "nickname2");

        assertThatThrownBy(() -> memberService.signup(member2))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Email already exists");
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void duplicateNickname_throwsException() {
        Member member1 = new Member("example1@google.com", "password1234", "nickname");
        memberService.signup(member1);
        Member member2 = new Member("example2@google.com", "password5678", "nickname");

        assertThatThrownBy(() -> memberService.signup(member2))
                .isInstanceOf(NicknameAlreadyExistsException.class)
                .hasMessage("Nickname already exists");
    }
}
