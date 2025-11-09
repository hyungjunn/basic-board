package io.github.hyungjun.backend.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hyungjun.backend.exception.EmailAlreadyExistsException;
import io.github.hyungjun.backend.exception.NicknameAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 API가 정상적으로 동작하여 사용자가 성공적으로 등록된다")
    void signup_success() throws JsonProcessingException {
        SignupRequest request = new SignupRequest(
                "test@email.com", "password1234", "nickname"
        );
        Member savedMember = new Member(1L, "test@email.com", "password1234", "nickname");
        given(memberService.signup(any(Member.class))).willReturn(savedMember);

        assertThat(mockMvc.post().uri("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatusOk()
                .hasHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .hasPathSatisfying("$.memberId", p -> assertThat(p).isEqualTo(1))
                .hasPathSatisfying("$.email", p -> assertThat(p).isEqualTo(request.email()))
                .hasPathSatisfying("$.nickname", p -> assertThat(p).isEqualTo(request.nickname()));

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        verify(memberService).signup(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo(request.email());
    }

    @Test
    @DisplayName("회원가입 중 이미 존재하는 이메일로 인해 실패하면 409 Conflict를 반환한다")
    void signup_emailValidationFailure() throws JsonProcessingException {
        SignupRequest invalidRequest = new SignupRequest(
                "test@email.com", "password1234", "nickname"
        );

        given(memberService.signup(any(Member.class)))
                .willThrow(new EmailAlreadyExistsException("Email already exists"));

        assertThat(mockMvc.post().uri("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .hasStatus(409)
                .hasHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .hasPathSatisfying("$.message", p -> assertThat(p).isEqualTo("Email already exists"));
    }

    @Test
    @DisplayName("회원가입 중 이미 존재하는 닉네임으로 인해 실패하면 409 Conflict를 반환한다")
    void signup_nicknameValidationFailure() throws JsonProcessingException {
        SignupRequest invalidRequest = new SignupRequest(
                "test@email.com", "password1234", "nickname"
        );

        given(memberService.signup(any(Member.class)))
                .willThrow(new NicknameAlreadyExistsException("Nickname already exists"));

        assertThat(mockMvc.post().uri("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .hasStatus(409)
                .hasHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .hasPathSatisfying("$.message", p -> assertThat(p).isEqualTo("Nickname already exists"));

    }
}
