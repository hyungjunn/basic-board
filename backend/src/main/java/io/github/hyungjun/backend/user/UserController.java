package io.github.hyungjun.backend.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users/signup")
    public UserSignupResponse signup(@RequestBody UserSignupRequest request) {
        return userService.signup(request);
    }
}

