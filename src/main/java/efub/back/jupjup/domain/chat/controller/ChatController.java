package efub.back.jupjup.domain.chat.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.chat.dto.ChatReqDto;
import efub.back.jupjup.domain.chat.service.ChatService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;

	// 개별 채팅 저장
	@PostMapping()
	public ResponseEntity<StatusResponse> saveChat(@AuthUser Member member, @Valid @RequestBody ChatReqDto chatReqDto) {
		return chatService.saveChat(chatReqDto, member);
	}

	// 채팅 기록 조회
	@GetMapping()
	public ResponseEntity<StatusResponse> getChatList(@AuthUser Member member) {
		return chatService.getChatList(member); // TODO : 여기서부터
	}

	// 게시글 찜하기 취소
	@DeleteMapping()
	public ResponseEntity<StatusResponse> unheartPost(@AuthUser Member member) {
		return chatService.resetChatRoom(member);
	}

}