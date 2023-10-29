package efub.back.jupjup.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BadgeInfo {
    private String code;
    private String title;
    private Integer count;
}
