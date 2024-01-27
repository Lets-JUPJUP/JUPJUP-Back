package efub.back.jupjup.domain.admin.trashCan.dto;

import efub.back.jupjup.domain.trashCan.domain.Feedback;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BinFeedbackStatisticDto {
	private Integer feedbackCode;
	private Long count;
	private String feedback;

	public static BinFeedbackStatisticDto from(Feedback feedback, Long count) {
		return BinFeedbackStatisticDto.builder()
			.feedbackCode(feedback.getCode())
			.count(count)
			.feedback(feedback.getDescription())
			.build();
	}
}
