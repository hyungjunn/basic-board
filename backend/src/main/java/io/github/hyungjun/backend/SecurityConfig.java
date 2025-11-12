package io.github.hyungjun.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // 지금은 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/members").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}
