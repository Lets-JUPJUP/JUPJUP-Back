package efub.back.jupjup.domain.chat.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.chat.domain.Chat;
import efub.back.jupjup.domain.chat.dto.ChatListResDto;
import efub.back.jupjup.domain.chat.dto.ChatReqDto;
import efub.back.jupjup.domain.chat.repository.ChatRepository;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatService {
	private final ChatRepository chatRepository;

	public ResponseEntity<StatusResponse> saveChat(ChatReqDto reqDto, Member member) {
		Chat chat = Chat.builder()
			.userId(member.getId())
			.role(reqDto.getRole())
			.type(reqDto.getType())
			.message(reqDto.getMessage())
			.timestamp(reqDto.getTimestamp())
			.build();
		Chat savedChat = chatRepository.save(chat);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(savedChat)
			.build());
	}

	public ResponseEntity<StatusResponse> getChatList(Member member) {
		List<Chat> chatList = chatRepository.findByUserIdOrderByTimestampAsc(member.getId());
		ChatListResDto resDto = ChatListResDto.builder()
			.count(chatList.size())
			.chatList(chatList)
			.build();
		return make200Response(resDto);
	}

	public ResponseEntity<StatusResponse> resetChatRoom(Member member) {
		chatRepository.deleteByUserId(member.getId());

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message("Chat room reset successful")
			.build());
	}

	private ResponseEntity<StatusResponse> make200Response(Object object) {
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(object)
			.build());
	}
}
