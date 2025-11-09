package io.github.hyungjun.backend.user;

import io.github.hyungjun.backend.exception.EmailAlreadyExistsException;
import io.github.hyungjun.backend.exception.NicknameAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class MemberServiceTest {

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        MemberRepository mockRepo = Mockito.mock(MemberRepository.class);
        Mockito.when(mockRepo.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(mockRepo.existsByNickname(anyString())).thenReturn(false);
        Mockito.when(mockRepo.save(any(Member.class))).thenAnswer(i -> i.getArgument(0));

        MemberService memberService = new MemberService(mockRepo);
        Member member = new Member("test@email.com", "password1234", "nickname");
        Member savedMember = memberService.signup(member);

        assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(member.getNickname()).isEqualTo(savedMember.getNickname());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void duplicateEmail_throwsException() {
        MemberRepository mockRepo = Mockito.mock(MemberRepository.class);
        Mockito.when(mockRepo.existsByEmail("test@email.com")).thenReturn(true);

        MemberService memberService = new MemberService(mockRepo);
        Member member = new Member("test@email.com", "password1234", "nickname");

        assertThatThrownBy(() -> memberService.signup(member))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Email already exists");;
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void duplicateNickname_throwsException() {
        MemberRepository mockRepo = Mockito.mock(MemberRepository.class);
        Mockito.when(mockRepo.existsByNickname("nickname")).thenReturn(true);

        MemberService memberService = new MemberService(mockRepo);
        Member member = new Member("test@email.com", "password1234", "nickname");

        assertThatThrownBy(() -> memberService.signup(member))
                .isInstanceOf(NicknameAlreadyExistsException.class)
                .hasMessage("Nickname already exists");
    }
}
