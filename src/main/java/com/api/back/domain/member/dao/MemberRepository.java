package com.api.back.domain.member.dao;

import com.api.back.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByName(String userName);

    Member findByGoogleId(String googleId);

    boolean existsByRefreshToken(String refreshToken);
}
