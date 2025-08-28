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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 API가 정상적으로 동작하여 사용자가 성공적으로 등록된다")
    void signup_success() throws JsonProcessingException {
        UserSignupRequest request = new UserSignupRequest(
                "test@email.com", "password1234", "nickname"
        );
        User savedUser = new User(1L, "test@email.com", "password1234", "nickname");
        given(userService.signup(any(User.class))).willReturn(savedUser);

        assertThat(mockMvc.post().uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatusOk()
                .hasHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .hasPathSatisfying("$.userId", p -> assertThat(p).isEqualTo(1))
                .hasPathSatisfying("$.email", p -> assertThat(p).isEqualTo(request.email()))
                .hasPathSatisfying("$.nickname", p -> assertThat(p).isEqualTo(request.nickname()));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).signup(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo(request.email());
    }

    @Test
    @DisplayName("회원가입 중 이미 존재하는 이메일로 인해 실패하면 409 Conflict를 반환한다")
    void signup_emailValidationFailure() throws JsonProcessingException {
        UserSignupRequest invalidRequest = new UserSignupRequest(
                "test@email.com", "password1234", "nickname"
        );

        given(userService.signup(any(User.class)))
                .willThrow(new EmailAlreadyExistsException("Email already exists"));

        assertThat(mockMvc.post().uri("/api/users")
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
        UserSignupRequest invalidRequest = new UserSignupRequest(
                "test@email.com", "password1234", "nickname"
        );

        given(userService.signup(any(User.class)))
                .willThrow(new NicknameAlreadyExistsException("Nickname already exists"));

        assertThat(mockMvc.post().uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .hasStatus(409)
                .hasHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .hasPathSatisfying("$.message", p -> assertThat(p).isEqualTo("Nickname already exists"));

    }
}
