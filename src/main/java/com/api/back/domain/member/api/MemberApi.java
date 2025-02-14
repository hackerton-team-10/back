package com.api.back.domain.member.api;

import com.api.back.domain.member.application.MemberService;
import com.api.back.domain.member.dto.response.MemberResponse;
import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberApi implements MemberApiDocs{

    private final MemberService memberService;

    /**
     * Init용 엔드포인트입니다.
     * **/
    @GetMapping({"/{memberId}", "/"}) // memberId가 없어도 호출 가능
    public ResponseEntity<WrapResponse<MemberResponse>> memberP(
        @PathVariable(value = "memberId", required = false) Optional<Long> id) {

        MemberResponse response = memberService.getMember(id.orElse(null));

        return ResponseEntity.ok(WrapResponse.create(response, SuccessType.SIMPLE_STATUS));
    }
}
