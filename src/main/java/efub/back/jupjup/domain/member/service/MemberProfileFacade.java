package efub.back.jupjup.domain.member.service;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.dto.response.ProfileStatResDto;
import efub.back.jupjup.domain.member.exception.MemberNotFoundException;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.postjoin.repository.PostjoinRepository;
import efub.back.jupjup.domain.score.domain.AverageScore;
import efub.back.jupjup.domain.score.repository.AverageScoreRepository;
import efub.back.jupjup.domain.userHeart.repository.UserHeartRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberProfileFacade {
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final PostjoinRepository postjoinRepository;
	private final UserHeartRepository userHeartRepository;
	private final AverageScoreRepository averageScoreRepository;

	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getUserProfileStats(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
		// 주최한 플로깅 개수
		int hostCount = (int)postRepository.countByAuthor(member);
		int joinCount = (int)postjoinRepository.countByMember(member);
		int postCount = hostCount + joinCount;
		int userHeartCount = userHeartRepository.countUserHeartsByMemberId(memberId).intValue();
		BigDecimal averageScore = averageScoreRepository.findById(memberId)
			.map(AverageScore::getAverageScore)
			.orElse(BigDecimal.ZERO);

		ProfileStatResDto profileStatResDto = ProfileStatResDto.builder()
			.averageScore(averageScore)
			.postCount(postCount)
			.userHeartsCount(userHeartCount)
			.build();

		return ResponseEntity.ok()
			.body(StatusResponse.builder()
				.status(StatusEnum.OK.getStatusCode())
				.message(StatusEnum.OK.getCode())
				.data(profileStatResDto)
				.build());
	}

}
