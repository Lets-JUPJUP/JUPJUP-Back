package efub.back.jupjup.domain.score.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import efub.back.jupjup.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AverageScore {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "average_score_id", updatable = false)
	private Long id;

	@Column(precision = 3, scale = 1)
	private BigDecimal averageScore;

	@Column(nullable = false)
	private Integer count;

	@Column(nullable = false)
	private Integer sum;

	@Column(nullable = false)
	private Long memberId;

	public void updateAverageScore(Integer score) {
		count += 1;
		sum += score;

		BigDecimal sumBD = BigDecimal.valueOf(sum);
		BigDecimal countBD = BigDecimal.valueOf(count);
		averageScore = sumBD.divide(countBD, 1, RoundingMode.HALF_UP);
	}

	public AverageScore(Member member) {
		this.averageScore = BigDecimal.valueOf(0);
		this.count = 0;
		this.sum = 0;
		this.memberId = member.getId();
	}

}
