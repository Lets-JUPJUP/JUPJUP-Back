package efub.back.jupjup.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WriterInfoDto {
	private Long writerId;
	private String nickname;
	private String profileImageUrl;
}
