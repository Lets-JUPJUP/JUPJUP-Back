package efub.back.jupjup.domain.notification.exception;

import efub.back.jupjup.global.exception.custom.NotFoundException;

public class NotificationNotFoundException extends NotFoundException {
	public NotificationNotFoundException(Long id) {
		super("id=" + id);
	}
}