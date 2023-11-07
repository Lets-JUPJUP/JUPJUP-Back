package efub.back.jupjup.domain.heart.dto;

import java.util.List;

import efub.back.jupjup.domain.post.dto.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeartResponseDto {
	private List<PostResponseDto> posts;
	private Long memberId;
	private Long count;
}
