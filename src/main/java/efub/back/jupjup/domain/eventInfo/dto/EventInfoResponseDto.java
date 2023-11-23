package efub.back.jupjup.domain.eventInfo.dto;

import efub.back.jupjup.domain.eventInfo.domain.EventInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventInfoResponseDto {
	private Long id;
	private String title;
	private String infoUrl;
	private String imageUrl;
	private boolean isJoined; // 참여 여부

	public static EventInfoResponseDto of(EventInfo eventInfo) {
		return EventInfoResponseDto.builder()
			.id(eventInfo.getId())
			.title(eventInfo.getTitle())
			.infoUrl(eventInfo.getInfoUrl())
			.imageUrl(eventInfo.getImageUrl())
			.isJoined(false)
			.build();
	}

	public static EventInfoResponseDto of(EventInfo eventInfo, boolean isJoined) {
		return EventInfoResponseDto.builder()
			.id(eventInfo.getId())
			.title(eventInfo.getTitle())
			.infoUrl(eventInfo.getInfoUrl())
			.imageUrl(eventInfo.getImageUrl())
			.isJoined(isJoined)
			.build();
	}
}
