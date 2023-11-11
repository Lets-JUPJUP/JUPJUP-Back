package efub.back.jupjup.domain.report.dto;

import java.time.LocalDateTime;
import java.util.List;

import efub.back.jupjup.domain.report.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {
	private  Long id;
	private Long writerId;
	private Long targetId;
	private String content;
	private List<String> fileUrls;
	private LocalDateTime createdDate;

	public static ReportResponseDto of(Report report, List<String> imgUrlList){
		return ReportResponseDto.builder()
			.id(report.getId())
			.writerId(report.getWriter().getId())
			.targetId(report.getTargetId())
			.content(report.getContent())
			.fileUrls(imgUrlList)
			.createdDate(report.getCreatedAt())
			.build();
	}
}
