package efub.back.jupjup.domain.score.service;

import java.math.BigDecimal;
import java.util.List;

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
import efub.back.jupjup.domain.score.exception.NotValidScoreException;
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

		// 리뷰 작성자가 플로깅 참여 멤버인지 확인
		checkMemberJoined(member.getId(), scoreReqDto.getPostId());

		// 이미 리뷰에 참여한 멤버인지 확인
		validateMemberHasNotScored(member.getId());

		// 유효한 값의 평점인지 확인
		validateScore(scoreReqDto.getScore());

		// 평점 데이터 저장
		Score score = scoreReqDto.toEntity(scoreReqDto, post, member);
		scoreRepository.save(score);

		// 평균 평점 저장
		AverageScore averageScore = new AverageScore(post.getAuthor());
		BigDecimal updatedAverageScore = averageScore.updateAverageScore(scoreReqDto.getScore());

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data("averageScore : " + updatedAverageScore)
			.build());
	}

	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getAverageScore(Long memberId) {
		BigDecimal result = averageScoreRepository.findById(memberId)
			.map(AverageScore::getAverageScore)
			.orElse(BigDecimal.ZERO);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data("averageScore : " + result)
			.build());
	}

	private void checkMemberJoined(Long memberId, Long postId) {
		List<Long> joinedMemberIds = postjoinRepository.findMemberIdsByPostId(postId);
		if (!joinedMemberIds.contains(memberId)) {
			throw new ScoringNotAllowedException();
		}
	}

	private Post checkPostExists(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		return post;
	}

	private void validateScore(Integer score) {
		if (score < 1 || score > 5) {
			throw new NotValidScoreException();
		}
	}

	private void validateMemberHasNotScored(Long memberId) {
		averageScoreRepository.findById(memberId).orElseThrow(AlreadyScoredException::new);
	}
}
