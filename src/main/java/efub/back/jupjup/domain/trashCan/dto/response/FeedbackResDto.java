package efub.back.jupjup.domain.trashCan.dto.response;

import efub.back.jupjup.domain.trashCan.domain.BinFeedback;
import efub.back.jupjup.domain.trashCan.domain.Feedback;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackResDto {
    private Long id;
    private Long trashCanId;
    private Integer feedbackCode;
    private String feedback;
    private Long memberId;
    private LocalDateTime createdAt;

    public static FeedbackResDto from(BinFeedback binFeedback){
        return FeedbackResDto.builder()
                .id(binFeedback.getId())
                .trashCanId(binFeedback.getTrashCanId())
                .feedbackCode(binFeedback.getFeedback().getCode())
                .feedback(binFeedback.getFeedback().getDescription())
                .memberId(binFeedback.getMember().getId())
                .createdAt(binFeedback.getCreatedAt())
                .build();
    }
}
