package io.github.hyungjun.backend.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserSignupResponse signup(UserSignupRequest request) {
        validateEmailNotDuplicated(request);
        validateNickNameNotDuplicated(request);
        User user = request.toUser();
        userRepository.save(user);
        return new UserSignupResponse(user);
    }

    private void validateNickNameNotDuplicated(UserSignupRequest request) {
        if (userRepository.existsByNickname(request.nickname())) {
            throw new IllegalArgumentException("Nickname already exists");
        }
    }

    private void validateEmailNotDuplicated(UserSignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
    }
}
