package efub.back.jupjup.domain.notification.dto;

import java.util.List;

import efub.back.jupjup.global.response.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotificationPageResDto {
	private List<NotificationResDto> notificationResDtos;
	private PageInfo pageInfo;
}

