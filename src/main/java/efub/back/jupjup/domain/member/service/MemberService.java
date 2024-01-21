package efub.back.jupjup.domain.member.service;

import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import efub.back.jupjup.domain.auth.dto.AccessTokenDto;
import efub.back.jupjup.domain.member.domain.AgeRange;
import efub.back.jupjup.domain.member.domain.Gender;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.dto.request.MemberReqDto;
import efub.back.jupjup.domain.member.dto.request.NicknameCheckReqDto;
import efub.back.jupjup.domain.member.dto.response.MemberResDto;
import efub.back.jupjup.domain.member.dto.response.MyProfileResDto;
import efub.back.jupjup.domain.member.dto.response.NicknameCheckResDto;
import efub.back.jupjup.domain.member.exception.MemberNotFoundException;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.global.jwt.JwtProvider;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;

	public Member findMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 멤버가 존재하지 않습니다."));
	}

	public ResponseEntity<StatusResponse> updateProfile(Member member, MemberReqDto memberReqDto) {
		member.updateNickname(memberReqDto.getNickname());
		member.updateGender(Gender.valueOf(memberReqDto.getGender()));
		member.updateAgeRange(AgeRange.valueOf(memberReqDto.getAgeRange()));
		member.updateProfileImage(memberReqDto.getProfileImage());
		Member updatedMember = memberRepository.save(member);
		MemberResDto memberResDto = MemberResDto.from(updatedMember);
		return make200Response(memberResDto);
	}

	public ResponseEntity<StatusResponse> readProfile(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
		MemberResDto memberResDto = MemberResDto.from(member);
		return make200Response(memberResDto);
	}

	public ResponseEntity<StatusResponse> getMyProfile(Member member) {
		MyProfileResDto myProfileResDto = MyProfileResDto.from(member);
		return make200Response(myProfileResDto);
	}

	public ResponseEntity<StatusResponse> checkDuplicateNickname(NicknameCheckReqDto reqDto, Member member) {
		Boolean isExistingNickname = null;
		String nickname = reqDto.getNickname();
		isExistingNickname = memberRepository.existsByNickname(nickname);

		// 자신의 기존 닉네임과 같은 닉네임인 경우
		if (nickname.equals(member.getNickname())) {
			isExistingNickname = false;
		}
		if (nickname.equals(Member.WITHDRAWN_NICKNAME)) {
			isExistingNickname = true;
		}
		
		NicknameCheckResDto resDto = new NicknameCheckResDto(isExistingNickname);
		return make200Response(resDto);
	}

	public Authentication withdraw(AccessTokenDto accessTokenDto) {
		String accessToken = accessTokenDto.getAccessToken();
		Authentication authentication = jwtProvider.getAuthentication(accessToken);
		String email = jwtProvider.tokenToEmail(accessToken);

		Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
		member.withdrawInfoProcess();
		return authentication;
	}

	private ResponseEntity<StatusResponse> make200Response(Object obj) {
		return ResponseEntity.ok()
			.body(StatusResponse.builder()
				.status(StatusEnum.OK.getStatusCode())
				.message(StatusEnum.OK.getCode())
				.data(obj)
				.build());
	}
}