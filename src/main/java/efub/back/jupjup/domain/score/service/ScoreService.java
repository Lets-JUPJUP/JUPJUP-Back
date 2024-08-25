package efub.back.jupjup.domain.score.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.exception.PostNotFoundException;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.postjoin.repository.PostjoinRepository;
import efub.back.jupjup.domain.score.domain.AverageScore;
import efub.back.jupjup.domain.score.domain.Score;
import efub.back.jupjup.domain.score.dto.request.ScoreReqDto;
import efub.back.jupjup.domain.score.exception.AlreadyScoredException;
import efub.back.jupjup.domain.score.exception.AuthorScoringNotAllowedException;
import efub.back.jupjup.domain.score.exception.ScoringNotAllowedException;
import efub.back.jupjup.domain.score.repository.AverageScoreRepository;
import efub.back.jupjup.domain.score.repository.ScoreRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ScoreService {
	private final PostjoinRepository postjoinRepository;
	private final PostRepository postRepository;
	private final ScoreRepository scoreRepository;
	private final AverageScoreRepository averageScoreRepository;

	public ResponseEntity<StatusResponse> giveScore(Member member, ScoreReqDto scoreReqDto) {
		// 존재하는 게시글인지 확인
		Post post = checkPostExists(scoreReqDto.getPostId());

		// 리뷰를 작성하려는 유저가 플로깅 참여 멤버인지 확인
		checkMemberJoined(member.getId(), post);

		// 이미 리뷰에 참여한 멤버인지 확인
		validateMemberHasNotScored(member, post);

		// 평점 데이터 저장
		Score score = scoreReqDto.toEntity(scoreReqDto, post, member);
		scoreRepository.save(score);

		// 평균 평점 저장
		Optional<AverageScore> existingAverageScoreOpt = averageScoreRepository.findByMemberId(
			post.getAuthor().getId());

		AverageScore averageScore;
		if (existingAverageScoreOpt.isPresent()) {
			// Update the existing AverageScore
			averageScore = existingAverageScoreOpt.get();
		} else {
			// Create a new AverageScore if it doesn't exist
			averageScore = new AverageScore(post.getAuthor());
			System.out.println(post.getAuthor().getId());
		}

		averageScore.updateAverageScore(scoreReqDto.getScore());
		AverageScore savedAverageScore = averageScoreRepository.save(averageScore);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data("averageScore : " + savedAverageScore.getAverageScore())
			.build());
	}

	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getAverageScore(Long memberId) {
		BigDecimal result = averageScoreRepository.findByMemberId(memberId)
			.map(AverageScore::getAverageScore)
			.orElse(BigDecimal.ZERO);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data("averageScore : " + result)
			.build());
	}

	private void checkMemberJoined(Long memberId, Post post) {
		List<Long> joinedMemberIds = postjoinRepository.findMemberIdsByPostId(post.getId());
		if (!joinedMemberIds.contains(memberId)) {
			throw new ScoringNotAllowedException();
		}

		// 주최자는 자신의 플로깅을 리뷰할 수 없음.
		if (memberId.equals(post.getAuthor().getId())) {
			throw new AuthorScoringNotAllowedException();
		}
	}

	private Post checkPostExists(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		return post;
	}

	private void validateMemberHasNotScored(Member member, Post post) {
		if (scoreRepository.existsByParticipantAndPost(member, post)) {
			throw new AlreadyScoredException();
		}
	}
}
