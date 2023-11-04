package efub.back.jupjup.domain.comment.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponseDto {
	private List<CommentDto> commentDtoList;
	private Integer commentNo;
}
