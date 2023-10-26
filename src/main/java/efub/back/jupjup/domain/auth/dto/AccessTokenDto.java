package efub.back.jupjup.domain.auth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessTokenDto {
    @NotBlank(message = "액세스 토큰 값은 blank일 수 없습니다.")
    private String accessToken;

    @Builder
    public AccessTokenDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
