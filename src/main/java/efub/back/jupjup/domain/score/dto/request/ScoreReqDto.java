package efub.back.jupjup.domain.score.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScoreReqDto {
	private Long postId;
	private Integer score;
}
