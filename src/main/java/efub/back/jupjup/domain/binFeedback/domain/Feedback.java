package efub.back.jupjup.domain.binFeedback.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Feedback {
    CLEAN("깨끗해요"),
    NEEDS_MAINTENANCE("관리가 필요해요"),
    MISSING_PLACE("위치가 정확하지 않아요");

    private final String description;
}
