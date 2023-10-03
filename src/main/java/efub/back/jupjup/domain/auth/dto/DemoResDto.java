package efub.back.jupjup.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "데모용 DTO")
public class DemoResDto {
    @Schema(description = "유저 id", nullable = false, example = "userIdExample1")
    private String userId;

    @Schema(description = "유저 이름", nullable = false, example = "홍길동")
    private String userName;
}
