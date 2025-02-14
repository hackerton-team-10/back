package com.api.back.domain.member.application;

import com.api.back.domain.member.dto.response.MemberResponse;
import com.api.back.domain.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    @Override
    public MemberResponse getMember(Long id) {

        if(id == null) {
            throw new MemberNotFoundException();
        }

        return MemberResponse.builder()
            .id(1L)
            .userName("testMember")
            .build();
    }
}
