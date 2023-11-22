package efub.back.jupjup.domain.eventjoin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventjoinResponseDto {
	private Long memberId;
	private Boolean eventJoined;
}
