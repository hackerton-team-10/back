package com.api.back.domain.member.application;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

import com.api.back.domain.member.domain.Member;
import com.api.back.domain.member.dto.response.MemberResponse;
import com.api.back.domain.member.exception.MemberNotFoundException;
import com.api.back.domain.member.repository.MemberRepository;
import com.api.back.global.error.exception.BusinessLogicException;
import com.api.back.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Override
    public MemberResponse getMember(Long userId){
        Member member = memberRepository.findById(userId).orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));
        return member.createMemberResponse();
    }
    @Override
    public void updateUserName(Long userId, String userName) {

        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new BusinessLogicException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateName(userName);

        memberRepository.save(member);

    }
}
