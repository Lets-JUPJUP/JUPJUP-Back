package efub.back.jupjup.domain.trashCan.domain;

import efub.back.jupjup.domain.trashCan.exception.FeedbackNotExistsForCodeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Feedback {
	NEEDS_MAINTENANCE(0, "관리 필요"),
	NORMAL(1, "보통"),
	EXCELLENT(2, "우수");

	private final Integer code;
	private final String description;

	public static Feedback getFeedbackByCode(Integer code) {
		for (Feedback feedback : Feedback.values()) {
			if (feedback.getCode().equals(code)) {
				return feedback;
			}
		}
		throw new FeedbackNotExistsForCodeException();
	}
}
