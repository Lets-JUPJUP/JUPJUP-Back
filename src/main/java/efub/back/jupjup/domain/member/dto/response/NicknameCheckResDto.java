package efub.back.jupjup.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NicknameCheckResDto {
    private Boolean isExistingNickname;
}
