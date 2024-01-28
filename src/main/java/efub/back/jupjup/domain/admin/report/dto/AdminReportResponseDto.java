package efub.back.jupjup.domain.admin.report.dto;

import efub.back.jupjup.domain.report.domain.Report;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Builder
public class AdminReportResponseDto {
	private Long id;
	private Long writerId;
	private Long targetId;
	private String content;
	private List<String> fileUrls;
	private LocalDateTime createdDate;
	private int reportCount;

	public static AdminReportResponseDto of(Report report, List<String> imgUrlList, int reportCount) {
		return AdminReportResponseDto.builder()
			.id(report.getId())
			.writerId(report.getWriter().getId())
			.targetId(report.getTargetId())
			.content(report.getContent())
			.fileUrls(imgUrlList)
			.createdDate(report.getCreatedAt())
			.reportCount(reportCount)
			.build();
	}
}
