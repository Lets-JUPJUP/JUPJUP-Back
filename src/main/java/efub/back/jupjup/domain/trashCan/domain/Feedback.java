package efub.back.jupjup.domain.trashCan.domain;

import efub.back.jupjup.domain.trashCan.exception.FeedbackNotExistsForCodeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Feedback {
	SUCCESSFUL_RECYCLING(0, "분리수거가 잘 되어 있어요"),
	CLEAN(1, "주변이 깨끗해요"),
	ACCESSIBILITY(2, "접근성이 좋아요"),
	DISPOSAL(3, "쓰레기를 자주 수거해가요"),
	ODOR(4, "악취가 나요"),
	MESSY(5, "주변이 지저분해요"),
	INSECT(6, "벌레가 많아요"),
	TORN(7, "봉투가 찢겨 있어요"),
	LOCATION(8, "위치가 잘못되었어요"),
	POOR_RECYCLING(9, "분리수거가 잘 되어 있지 않아요");

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
