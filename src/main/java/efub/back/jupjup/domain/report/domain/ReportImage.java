package efub.back.jupjup.domain.report.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReportImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(length = 9999)
	private String fileUrl;

	@ManyToOne
	@JoinColumn(name = "report_id")
	private Report report;

	public ReportImage(String fileUrl, Report report) {
		this.fileUrl = fileUrl;
		this.report = report;
	}
}
