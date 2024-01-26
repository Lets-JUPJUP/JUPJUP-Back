package efub.back.jupjup.domain.admin.event.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import efub.back.jupjup.domain.admin.event.dto.AdminEventRequestDto;
import efub.back.jupjup.domain.admin.event.service.AdminEventService;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins/eventInfos")
public class AdminEventController {
	private final AdminEventService adminEventService;

	// 공식행사 게시글 작성하기
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<StatusResponse> createEvent(@RequestBody AdminEventRequestDto requestDto) {
		return adminEventService.createEvent(requestDto);
	}

	// 공식행사 게시글 리스트 조회
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/lists")
	public ResponseEntity<StatusResponse> getAllEventInfos() {
		return adminEventService.getAllEventInfos();
	}

	// 공식행사 게시글 삭제
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{eventId}")
	public ResponseEntity<StatusResponse> deleteEvent(@PathVariable Long eventId) {
		return adminEventService.deleteEvent(eventId);
	}
}
