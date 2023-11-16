package efub.back.jupjup.domain.eventcomment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.eventcomment.dto.EventcommentRequestDto;
import efub.back.jupjup.domain.eventcomment.service.EventcommentService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/eventInfos/{eventInfoId}/comments")
public class EventcommentController {
	private final EventcommentService eventcommentService;

	@PostMapping
	public ResponseEntity<StatusResponse> addComment(@PathVariable Long eventInfoId,
		@RequestBody EventcommentRequestDto requestDto,
		@AuthUser Member writer){
		return eventcommentService.addEventcomment(eventInfoId, requestDto, writer);
	}

	@GetMapping
	public ResponseEntity<StatusResponse> listComments(@PathVariable Long eventInfoId) {
		return eventcommentService.getAllEventcomments(eventInfoId);
	}

	@DeleteMapping("/{eventCommentId}")
	public ResponseEntity<StatusResponse> deleteComment(@PathVariable Long eventInfoId,
		@PathVariable Long eventCommentId,
		@AuthUser Member writer) {
		return eventcommentService.deleteEventcomment(eventInfoId, eventCommentId, writer);
	}
}
