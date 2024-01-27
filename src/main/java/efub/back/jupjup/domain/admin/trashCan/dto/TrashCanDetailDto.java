package efub.back.jupjup.domain.admin.trashCan.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrashCanDetailDto {
	private TrashCanDto trashCanDto;
	private Long feedbackCount;
	private List<BinFeedbackStatisticDto> feedbacks;
}
