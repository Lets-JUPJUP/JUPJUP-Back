package efub.back.jupjup.domain.score.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.exception.PostNotFoundException;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.postjoin.repository.PostjoinRepository;
import efub.back.jupjup.domain.score.dto.request.ScoreReqDto;
import efub.back.jupjup.domain.score.exception.NotValidScoreException;
import efub.back.jupjup.domain.score.exception.ScoringNotAllowedException;
import efub.back.jupjup.global.redis.RedisReviewCountService;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ScoreService {
	private final RedisReviewCountService reviewCountService;
	private final MemberRepository memberRepository;
	private final PostjoinRepository postjoinRepository;
	private final PostRepository postRepository;

	public ResponseEntity<StatusResponse> giveScore(Member member, ScoreReqDto scoreReqDto) {
		// 존재하는 게시글인지 확인
		checkPostExists(scoreReqDto.getPostId());

		// 리뷰 작성자가 플로깅 참여 멤버인지 확인
		checkMemberJoined(member.getId(), scoreReqDto.getPostId());

		// 유효한 값의 평점인지 확인
		validateScore(scoreReqDto.getScore());

		// TODO : Post에 평균 평점 값 추가하고 업데이트

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(null)
			.build());
	}

	private void checkMemberJoined(Long memberId, Long postId) {
		List<Long> joinedMemberIds = postjoinRepository.findMemberIdsByPostId(postId);
		if (!joinedMemberIds.contains(memberId)) {
			throw new ScoringNotAllowedException();
		}
	}

	private void checkPostExists(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
	}

	private void validateScore(Integer score) {
		if (score < 1 || score > 5) {
			throw new NotValidScoreException();
		}
	}
}
