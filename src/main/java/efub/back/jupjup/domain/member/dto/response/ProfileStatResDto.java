package efub.back.jupjup.domain.member.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileStatResDto {
	private int userHeartsCount;
	private BigDecimal averageScore; // 평균 평점
	private int postCount;
}
