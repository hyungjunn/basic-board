package io.github.hyungjun.backend.user;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    void signup_success() throws Exception {
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
}
