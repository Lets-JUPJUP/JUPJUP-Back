package efub.back.jupjup.domain.score.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import efub.back.jupjup.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AverageScore {
	@Id
	private Long memberId;

	@Column(precision = 3, scale = 1)
	private BigDecimal averageScore;

	@Column(nullable = false)
	private Integer count;

	@Column(nullable = false)
	private Integer sum;

	@OneToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
	@MapsId(value = "memberId")
	@JoinColumn(name = "member_id")
	private Member member;

	public BigDecimal updateAverageScore(Integer score) {
		count += 1;
		sum += score;

		BigDecimal sumBD = BigDecimal.valueOf(sum);
		BigDecimal countBD = BigDecimal.valueOf(count);
		averageScore = sumBD.divide(countBD, 1, RoundingMode.HALF_UP);
		return averageScore;
	}

	public AverageScore(Member member) {
		this.memberId = member.getId();
		this.averageScore = BigDecimal.valueOf(0);
		this.count = 0;
		this.sum = 0;
		this.member = member;
	}

}
