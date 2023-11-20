package efub.back.jupjup.domain.notification.dto;

import java.time.LocalDateTime;

import efub.back.jupjup.domain.notification.domain.Notification;
import efub.back.jupjup.domain.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotificationResDto {
	private Long id;
	private String content;
	private Long contentId;
	private Boolean isRead;
	private NotificationType notificationType;
	private LocalDateTime time;

	public static NotificationResDto create(Notification notification) {
		return new NotificationResDto(notification.getId(), notification.getContent(), notification.getContentId(),
			notification.getIsRead(), notification.getNotificationType(), notification.getCreatedAt());
	}
}
