package efub.back.jupjup.domain.postjoin.dto;

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
public class PostjoinResponseDto {
	private List<PostResponseDto> posts;
	private Long memberId;
	private Boolean isJoined;
}
