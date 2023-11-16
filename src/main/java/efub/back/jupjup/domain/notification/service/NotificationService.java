package efub.back.jupjup.domain.notification.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.notification.EmitterRepositoryImpl;
import efub.back.jupjup.domain.notification.comment.domain.Notification;
import efub.back.jupjup.domain.notification.comment.domain.NotificationType;
import efub.back.jupjup.domain.notification.dto.NotificationPageResDto;
import efub.back.jupjup.domain.notification.dto.NotificationResDto;
import efub.back.jupjup.domain.notification.exception.NotificationNotFoundException;
import efub.back.jupjup.domain.notification.repository.NotificationRepository;
import efub.back.jupjup.global.response.PageInfo;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
	private final EmitterRepositoryImpl emitterRepository;
	private final NotificationRepository notificationRepository;

	public SseEmitter subscribe(Long memberId, String lastEventId) {
		String emitterId = makeUniqueEmitterId(memberId);
		Long timeout = 60L * 1000L * 60L; // 1시간
		SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(timeout));

		sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
		sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

		String eventId = makeUniqueEmitterId(memberId);
		sendNotification(sseEmitter, eventId, emitterId, "EventStream Created. [memberId=" + memberId + "]");

		if (hasLostData(lastEventId)) {
			sendLostData(lastEventId, memberId, emitterId, sseEmitter);
		}
		return sseEmitter;
	}

	private String makeUniqueEmitterId(Long memberId) {
		return memberId + "_" + System.currentTimeMillis();
	}

	// timeout 되면 503 에러 발생 -> 더미데이터 발행
	private void sendNotification(SseEmitter sseEmitter, String eventId, String emitterId, Object data) {
		try {
			sseEmitter.send(SseEmitter.event()
				.id(eventId)
				.data(data));
		} catch (IOException exception) {
			emitterRepository.deleteById(emitterId);
		}
	}

	// 받지 못한 알림이 있는지 확인
	// lastEventId가 null이 아니면 받지 못한 알림이 있는 것
	private boolean hasLostData(String lastEventId) {
		return !lastEventId.isEmpty();
	}

	// 받지 못한 알림은 lastEventId를 기준으로 그 이후의 데이터를 추출해 알림을 보냄
	private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
		Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithMemberId(
			String.valueOf(memberId));
		eventCaches.entrySet().stream()
			.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
			.forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
	}

	@Async
	public void send(Member receiver, NotificationType notificationType, String content, Long contentId) {
		Notification notification = notificationRepository.save(
			createNotification(receiver, notificationType, content, contentId));
		String receiverId = String.valueOf(receiver.getId());
		String eventId = receiverId + "_" + System.currentTimeMillis();
		Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersStartWithMemberId(receiverId);
		emitters.forEach((key, emitter) -> {
			emitterRepository.saveEventCache(key, notification);
			sendNotification(emitter, eventId, key, NotificationResDto.create(notification));
		});

	}

	private Notification createNotification(Member receiver, NotificationType notificationType, String content,
		Long contentId) {
		return Notification.builder()
			.receiver(receiver)
			.notificationType(notificationType)
			.content(content)
			.contentId(contentId)
			.isRead(false) //읽지 않음 상태로 초기 세팅
			.build();
	}

	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> findAllNotification(Long memberId, int page, int size) {
		List<NotificationType> types = Arrays.asList(NotificationType.FLOGGING, NotificationType.REPLY,
			NotificationType.COMMENT);
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Notification> notifications = notificationRepository.findAllByReceiverIdAndNotificationTypeIn(memberId,
			types, pageRequest);
		List<NotificationResDto> notificationResDtos = notifications.stream()
			.map(NotificationResDto::create)
			.collect(Collectors.toList());
		NotificationPageResDto resDto = new NotificationPageResDto(notificationResDtos, createPageInfo(notifications));

		return ResponseEntity.ok(createStatusResponse(resDto));
	}

	@Transactional
	public ResponseEntity<StatusResponse> readAllNotification(Member member) {
		List<NotificationType> types = Arrays.asList(NotificationType.FLOGGING, NotificationType.REPLY,
			NotificationType.COMMENT);

		List<Notification> notifications = notificationRepository.findAllByReceiverIdAndNotificationTypeIn(
			member.getId(), types);
		for (Notification notification : notifications) {
			notification.read();
		}

		return ResponseEntity.ok(createStatusResponse("알림 전체 읽음 처리 완료"));
	}

	@Transactional
	public ResponseEntity<StatusResponse> readNotification(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new NotificationNotFoundException(notificationId));
		notification.read();
		return ResponseEntity.ok(createStatusResponse("알림 읽음 처리 완료"));
	}

	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> countUnreadCommentNotifications(Member member) {
		List<NotificationType> types = Arrays.asList(NotificationType.FLOGGING, NotificationType.REPLY,
			NotificationType.COMMENT);
		long unreadCount = notificationRepository.countByIsReadFalseAndReceiverAndNotificationTypeIn(member, types);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(String.format("unReadCount :%d", unreadCount))
			.build());
	}

	private PageInfo createPageInfo(Page<?> page) {
		return new PageInfo(page.getNumber(), page.getNumberOfElements(), (int)page.getTotalElements(),
			page.getTotalPages());
	}

	private StatusResponse createStatusResponse(Object data) {
		return StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(data)
			.build();
	}
}
