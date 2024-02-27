package efub.back.jupjup.domain.notification.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.google.firebase.messaging.FirebaseMessagingException;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.notification.dto.FcmRequestDto;
import efub.back.jupjup.domain.notification.dto.TestTokenDto;
import efub.back.jupjup.domain.notification.service.FirebaseService;
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
	private final FirebaseService firebaseService;

	@PostMapping("/fcm")
	public ResponseEntity pushMessageTopic(@RequestBody FcmRequestDto requestDTO) throws
		IOException,
		FirebaseMessagingException {
		System.out.println(requestDTO.getTargetToken() + " "
			+ requestDTO.getTitle() + " " + requestDTO.getBody());

		firebaseService.sendMessageByTopic(
			requestDTO.getTitle(),
			requestDTO.getBody());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/token")
	public ResponseEntity pushMessageToken(@RequestBody FcmRequestDto requestDTO) throws
		IOException,
		FirebaseMessagingException {
		System.out.println(requestDTO.getTargetToken() + " "
			+ requestDTO.getTitle() + " " + requestDTO.getBody());

		firebaseService.sendMessageByToken(
			requestDTO.getTitle(),
			requestDTO.getBody(),
			requestDTO.getTargetToken());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/test")
	public void test(@RequestBody TestTokenDto dto) {
		log.info(dto.getToken());
	}

	//알림 구독 (SSE)
	@GetMapping(value = "/subscribe", produces = "text/event-stream")
	public SseEmitter subscribe(@AuthUser Member member,
		@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
		final HttpServletResponse response) {
		response.setHeader("Connection", "keep-alive");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("X-Accel-Buffering", "no");
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
