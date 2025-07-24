package io.github.hyungjun.backend.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/api/users/signup")
    public UserSignupResponse signup(@RequestBody UserSignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = userRepository.save(request.toUser());
        return new UserSignupResponse(user);
    }

    record UserSignupRequest(String email, String password, String nickname) {
        public User toUser() {
            return new User(email, password, nickname);
        }
    }

    record UserSignupResponse(String email, String password, String nickname) {
        public UserSignupResponse(User user) {
            this(user.getEmail(), user.getPassword(), user.getNickname());
        }
    }
}

