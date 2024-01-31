package efub.back.jupjup.domain.admin.report.dto;

import efub.back.jupjup.domain.report.domain.Report;
import efub.back.jupjup.domain.report.domain.ReportImage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

	public static AdminReportResponseDto of(Report report, int reportCount) {
		List<String> fileUrls = report.getReportImages().stream()
			.map(ReportImage::getFileUrl)
			.collect(Collectors.toList());
		return AdminReportResponseDto.builder()
			.id(report.getId())
			.writerId(report.getWriter().getId())
			.targetId(report.getTargetId())
			.content(report.getContent())
			.fileUrls(fileUrls)
			.createdDate(report.getCreatedAt())
			.reportCount(reportCount)
			.build();
	}
}
