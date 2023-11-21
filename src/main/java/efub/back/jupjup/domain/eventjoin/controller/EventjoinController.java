package efub.back.jupjup.domain.eventjoin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.eventInfo.domain.EventInfo;
import efub.back.jupjup.domain.eventInfo.repository.EventInfoRepository;
import efub.back.jupjup.domain.eventjoin.service.EventjoinService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/eventinfos")
@RequiredArgsConstructor
public class EventjoinController {
	private final EventjoinService eventjoinService;
	private final EventInfoRepository eventInfoRepository;

	// 게시글에 참여
	@PostMapping("/{eventInfoId}/join")
	public ResponseEntity<StatusResponse> joinPost(@PathVariable Long eventInfoId, @AuthUser Member member) {
		EventInfo eventInfo = eventInfoRepository.findById(eventInfoId).orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
		return eventjoinService.joinEvent(member, eventInfoId);
	}

	// 게시글 참여 취소
	@DeleteMapping("/{eventInfoId}/join")
	public ResponseEntity<StatusResponse> unjoinPost(@PathVariable Long eventInfoId, @AuthUser Member member) {
		return eventjoinService.unjoinEvent(member, eventInfoId);
	}

	// 공식행사 참여 인원 수 조회
	@GetMapping("/{eventInfoId}/count")
	public ResponseEntity<StatusResponse> countParticipants(@PathVariable Long eventInfoId) {
		return eventjoinService.countEventParticipants(eventInfoId);
	}
}
