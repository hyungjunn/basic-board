package io.github.hyungjun.backend.user;

import io.github.hyungjun.backend.exception.EmailAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signup(User user) {
        validateEmailNotDuplicated(user.getEmail());
        validateNickNameNotDuplicated(user.getNickname());
        return userRepository.save(user);
    }

    private void validateNickNameNotDuplicated(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("Nickname already exists");
        }
    }

    private void validateEmailNotDuplicated(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }
}
