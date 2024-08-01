package efub.back.jupjup.domain.score.dto.request;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.score.domain.Score;
import efub.back.jupjup.domain.score.exception.NotValidScoreException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScoreReqDto {
	private Long postId;
	private Integer score;

	public Score toEntity(ScoreReqDto reqDto, Post post, Member member) {
		validateScore(score);
		return Score.builder()
			.score(score)
			.participant(member)
			.post(post)
			.build();
	}

	private void validateScore(Integer score) {
		if (score < 1 || score > 5) {
			throw new NotValidScoreException();
		}
	}
}
