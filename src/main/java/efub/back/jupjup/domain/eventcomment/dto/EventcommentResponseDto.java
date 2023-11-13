package efub.back.jupjup.domain.eventcomment.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EventcommentResponseDto {
	private List<EventcommentDto> eventcommentDtoList;
	private Integer eventcommentNo;
}
