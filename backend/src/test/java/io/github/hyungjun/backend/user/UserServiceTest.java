package io.github.hyungjun.backend.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class UserServiceTest {

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        UserRepository mockRepo = Mockito.mock(UserRepository.class);
        Mockito.when(mockRepo.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(mockRepo.existsByNickname(anyString())).thenReturn(false);
        Mockito.when(mockRepo.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserService userService = new UserService(mockRepo);
        User user = new User("test@email.com", "password1234", "nickname");
        User savedUser = userService.signup(user);

        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(user.getNickname()).isEqualTo(savedUser.getNickname());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void duplicateEmail_throwsException() {
        UserRepository mockRepo = Mockito.mock(UserRepository.class);
        Mockito.when(mockRepo.existsByEmail("test@email.com")).thenReturn(true);

        UserService userService = new UserService(mockRepo);
        User user = new User("test@email.com", "password1234", "nickname");

        Assertions.assertThatThrownBy(() -> userService.signup(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");;
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void duplicateNickname_throwsException() {
        UserRepository mockRepo = Mockito.mock(UserRepository.class);
        Mockito.when(mockRepo.existsByNickname("nickname")).thenReturn(true);

        UserService userService = new UserService(mockRepo);
        User user = new User("test@email.com", "password1234", "nickname");

        Assertions.assertThatThrownBy(() -> userService.signup(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nickname already exists");
    }
}
