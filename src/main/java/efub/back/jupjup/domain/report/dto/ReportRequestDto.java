package efub.back.jupjup.domain.report.dto;

import java.util.List;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.report.domain.Report;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportRequestDto {
	private Long targetId;
	private String content;
	private List<String> images;

	public Report toEntity(ReportRequestDto dto, Member writer) {
		return Report.builder()
			.targetId(dto.getTargetId())
			.content(dto.getContent())
			.writer(writer)
			.build();
	}
}
