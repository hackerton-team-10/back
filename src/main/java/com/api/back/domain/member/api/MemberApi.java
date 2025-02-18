package com.api.back.domain.member.api;

import com.api.back.domain.member.application.MemberService;
import com.api.back.domain.member.dto.response.MemberResponse;
import com.api.back.global.common.response.SuccessType;
import com.api.back.global.common.response.WrapResponse;
import com.api.back.global.config.security.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberApi implements MemberApiDocs {

    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<WrapResponse<MemberResponse>> member(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        MemberResponse response = memberService.getMember(customOAuth2User.getUserName());
        return ResponseEntity.ok(WrapResponse.create(response, SuccessType.SIMPLE_STATUS));
    }

    /**
     * 사용자 이름 변경 엔드포인트
     **/
    @PatchMapping()
    public ResponseEntity<WrapResponse<SuccessType>> memberP(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestParam("userName") String userName) {

        log.info(String.valueOf(customOAuth2User.getUserName()));

        memberService.updateUserName(customOAuth2User.getUserName(), userName);

        return ResponseEntity.ok(WrapResponse.create(SuccessType.SIMPLE_STATUS));
    }
}
