package efub.back.jupjup.domain.notification.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.notification.domain.Notification;
import efub.back.jupjup.domain.notification.domain.NotificationType;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	Page<Notification> findAllByReceiverIdAndNotificationTypeIn(Long receiverId, List<NotificationType> types,
		Pageable pageable);

	List<Notification> findAllByReceiverIdAndNotificationTypeIn(Long receiverId, List<NotificationType> types);

	long countByIsReadFalseAndReceiverAndNotificationTypeIn(Member receiver, List<NotificationType> types);
}
