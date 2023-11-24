package efub.back.jupjup.domain.trashCan.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class FeedbackListResDto {
	private Integer size;
	private List<FeedbackResDto> binFeedbacks;

	public FeedbackListResDto(Integer size, List<FeedbackResDto> binFeedbacks) {
		this.size = size;
		this.binFeedbacks = binFeedbacks;
	}
}
