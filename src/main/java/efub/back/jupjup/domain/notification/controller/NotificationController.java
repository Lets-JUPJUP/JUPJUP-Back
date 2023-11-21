package efub.back.jupjup.domain.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.notification.service.NotificationService;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationService notificationService;

	//알림 구독 (SSE)
	@GetMapping(value = "/subscribe", produces = "text/event-stream")
	public SseEmitter subscribe(@AuthUser Member member,
		@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		return notificationService.subscribe(member.getId(), lastEventId);
	}

	// 전체 알림 읽음 처리
	@PostMapping("/read/list")
	public ResponseEntity<StatusResponse> readAllNotification(@AuthUser Member member) {
		return notificationService.readAllNotification(member);
	}

	// 단일 알림 읽음 처리
	@PostMapping("/read/{notificationId}")
	public ResponseEntity<StatusResponse> readNotification(@PathVariable Long notificationId) {
		return notificationService.readNotification(notificationId);
	}

	// 전체 알림 조회
	@GetMapping("/list")
	public ResponseEntity<StatusResponse> getNotifications(@AuthUser Member member,
		@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		if (page == null || size == null) {
			return notificationService.findAllNotification(member.getId());
		}
		return notificationService.findAllNotification(member.getId(), page, size);
	}

	//읽지 않은 알림 개수 조회
	@GetMapping("/count")
	public ResponseEntity<StatusResponse> countUnreadNotifications(@AuthUser Member member) {
		return notificationService.countUnreadCommentNotifications(member);
	}

}
