package com.api.back.domain.member.application;

import com.api.back.domain.member.dto.response.MemberResponse;

public interface MemberService {

    MemberResponse getMember(Long id);
}
