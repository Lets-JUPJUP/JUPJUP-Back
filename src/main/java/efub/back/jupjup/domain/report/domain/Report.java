package efub.back.jupjup.domain.report.domain;

import efub.back.jupjup.global.BaseTimeEntity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Report extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long reportId;

	@Column
	private String content;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	public Report(Long memberId, String content){
		this.memberId = memberId;
		this.content = content;
	}
}
