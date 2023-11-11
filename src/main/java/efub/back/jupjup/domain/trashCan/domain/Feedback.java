package efub.back.jupjup.domain.trashCan.domain;

import efub.back.jupjup.domain.review.domain.Badge;
import efub.back.jupjup.domain.trashCan.exception.FeedbackNotExistsForCodeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Feedback {
    CLEAN(1, "깨끗해요"),
    NEEDS_MAINTENANCE(2, "관리가 필요해요"),
    MISSING_PLACE(3,"위치가 정확하지 않아요");

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
