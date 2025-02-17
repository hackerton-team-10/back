package com.api.back.domain.member.domain;

import com.api.back.global.common.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "google_id", nullable = false, unique = true)
    private String googleId;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "profile", nullable = false)
    private String profile;

    @Column(name = "role", nullable = false, length = 10)
    private String role;

    @Column(name = "refresh_token", unique = false, length = 500)
    private String refreshToken;


    public void updateName(String name) {this.name = name;}

    public void updateEmail(String email) {this.email = email;}

    public void updateRefreshToken(String refreshToken) {this.refreshToken = refreshToken;}

}