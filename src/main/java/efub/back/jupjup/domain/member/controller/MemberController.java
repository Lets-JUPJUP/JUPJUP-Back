package efub.back.jupjup.domain.member.controller;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.dto.request.MemberReqDto;
import efub.back.jupjup.domain.member.dto.request.NicknameCheckReqDto;
import efub.back.jupjup.domain.member.service.MemberService;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<StatusResponse> readProfile(@PathVariable Long memberId){
        return memberService.readProfile(memberId);
    }

    @PutMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> updateProfile(@AuthUser Member member, @RequestBody MemberReqDto memberReqDto){
        return memberService.updateProfile(member, memberReqDto);
    }

    @PostMapping("/checkNickname")
    public ResponseEntity<StatusResponse> checkDuplicateNickname(@RequestBody NicknameCheckReqDto nicknameCheckReqDto){
        return memberService.checkDuplicateNickname(nicknameCheckReqDto);
    }
}
