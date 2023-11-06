package efub.back.jupjup.domain.review.dto.response;

import efub.back.jupjup.domain.review.domain.Badge;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Top3BadgeResDto {
    private Integer badgeCount;
    private List<BadgeInfo> badges;
}
