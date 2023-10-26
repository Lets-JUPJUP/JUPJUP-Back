package efub.back.jupjup.domain.member.service;

import efub.back.jupjup.domain.member.domain.Gender;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.dto.request.MemberReqDto;
import efub.back.jupjup.domain.member.dto.request.NicknameCheckReqDto;
import efub.back.jupjup.domain.member.dto.response.MemberResDto;
import efub.back.jupjup.domain.member.dto.response.NicknameCheckResDto;
import efub.back.jupjup.domain.member.exception.MemberNotFoundException;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    public ResponseEntity<StatusResponse> updateProfile(Member member, MemberReqDto memberReqDto) {
        member.updateNickname(memberReqDto.getNickname());
        member.updateGender(Gender.valueOf(memberReqDto.getGender()));
        member.updateProfileImage(memberReqDto.getProfileImage());
        Member updatedMember = memberRepository.save(member);
        MemberResDto memberResDto = MemberResDto.from(updatedMember);
        return make200Response(memberResDto);
    }

    public ResponseEntity<StatusResponse> readProfile(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        MemberResDto memberResDto = MemberResDto.from(member);
        return make200Response(memberResDto);
    }

    public ResponseEntity<StatusResponse> checkDuplicateNickname(NicknameCheckReqDto reqDto){
        boolean isExistingNickname = memberRepository.existsByNickname(reqDto.getNickname());
        NicknameCheckResDto resDto = new NicknameCheckResDto(isExistingNickname);
        return make200Response(resDto);
    }

    private ResponseEntity<StatusResponse> make200Response(Object obj){
        return ResponseEntity.ok()
                .body(StatusResponse.builder()
                        .status(StatusEnum.OK.getStatusCode())
                        .message(StatusEnum.OK.getCode())
                        .data(obj)
                        .build());
    }
}
