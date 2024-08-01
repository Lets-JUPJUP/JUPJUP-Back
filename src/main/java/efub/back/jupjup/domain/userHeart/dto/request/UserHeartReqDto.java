package efub.back.jupjup.domain.userHeart.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHeartReqDto {
	private Long postId;
	private Long targetId;
}
