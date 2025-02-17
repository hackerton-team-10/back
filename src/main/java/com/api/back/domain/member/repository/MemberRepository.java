package com.api.back.domain.member.repository;

import com.api.back.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByName(String userName);

    Optional<Member> findByGoogleId(String googleId);

    boolean existsByRefreshToken(String refreshToken);

}
