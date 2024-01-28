package efub.back.jupjup.domain.admin.event.dto;

import efub.back.jupjup.domain.eventInfo.domain.EventInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminEventResponseDto {
	private Long id;
	private String title;
	private String infoUrl;
	private String imageUrl;

	public static AdminEventResponseDto of(EventInfo eventInfo) {
		return AdminEventResponseDto.builder()
			.id(eventInfo.getId())
			.title(eventInfo.getTitle())
			.infoUrl(eventInfo.getInfoUrl())
			.imageUrl(eventInfo.getImageUrl())
			.build();
	}
}
