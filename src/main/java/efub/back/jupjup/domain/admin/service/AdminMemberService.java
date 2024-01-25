package efub.back.jupjup.domain.admin.service;

import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import efub.back.jupjup.domain.auth.service.AuthService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.domain.MemberStatus;
import efub.back.jupjup.domain.member.exception.MemberNotFoundException;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminMemberService {

	private final MemberRepository memberRepository;
	private final AuthService authService;

	public ResponseEntity<StatusResponse> blockMember(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
		member.updateMemberStatus(MemberStatus.BLOCKED);
		authService.blockInRedis(member.getEmail());
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data("회원 탈퇴에 성공했습니다.")
			.build());
	}
}
