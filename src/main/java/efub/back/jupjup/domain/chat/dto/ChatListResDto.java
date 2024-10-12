package efub.back.jupjup.domain.chat.dto;

import java.util.List;

import efub.back.jupjup.domain.chat.domain.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatListResDto {
	private int count;
	private List<Chat> chatList;
}
