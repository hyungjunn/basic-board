package io.github.hyungjun.backend.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

@Entity
@Table(name = "members", uniqueConstraints = {
        @UniqueConstraint(name = "uk_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_nickname", columnNames = {"nickname"})
})
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String nickname;

    public static Member signup(String email, String encodedPassword, String nickname) {
        return new Member(email, encodedPassword, nickname);
    }

    protected Member() {}

    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public Member(Long id, String email, String password, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
