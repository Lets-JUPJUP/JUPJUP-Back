package efub.back.jupjup.domain.member.service;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	private final MemberRepository memberRepository;
	@Transactional(readOnly = true)
	public Member findMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 멤버가 존재하지 않습니다."));
	}
}

