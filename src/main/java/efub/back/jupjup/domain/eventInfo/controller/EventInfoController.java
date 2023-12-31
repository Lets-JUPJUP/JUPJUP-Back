package efub.back.jupjup.domain.eventInfo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.eventInfo.service.EventInfoService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/eventInfos")
public class EventInfoController {
	private final EventInfoService eventInfoService;

	// 공식행사 게시글 상세 조회
	@GetMapping("/{eventInfoId}")
	public ResponseEntity<StatusResponse> getEventInfo(@PathVariable Long eventInfoId, @AuthUser Member member){
		return eventInfoService.getEventInfo(eventInfoId, member);
	}

	// 공식행사 게시글 리스트 조회
	@GetMapping("/lists")
	public ResponseEntity<StatusResponse> getAllEventInfos() {
		return eventInfoService.getAllEventInfos();
	}

}
