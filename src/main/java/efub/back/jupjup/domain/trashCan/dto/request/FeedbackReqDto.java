package efub.back.jupjup.domain.trashCan.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedbackReqDto {
    private Long trashCanId;
    private Integer feedbackCode;
}
