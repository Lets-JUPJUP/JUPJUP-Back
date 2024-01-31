package efub.back.jupjup.domain.report.domain;

import java.util.List;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.global.BaseTimeEntity;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Report extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", nullable = false)
	private Member writer;

	@Column(nullable = false)
	private Long targetId;

	@Column(length = 500, nullable = false)
	private String content;

	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ReportImage> reportImages;

	@Builder
	public Report(Member writer, Long targetId, String content){
		this.writer = writer;
		this.targetId = targetId;
		this.content = content;
	}
}
