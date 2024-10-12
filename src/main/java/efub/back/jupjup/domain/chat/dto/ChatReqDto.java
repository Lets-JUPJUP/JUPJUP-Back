package efub.back.jupjup.domain.chat.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import efub.back.jupjup.domain.chat.domain.ChatRole;
import efub.back.jupjup.domain.chat.domain.ChatType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatReqDto {
	// 공통 유효성 검증 메시지 상수
	private static final String INVALID_DATA_MESSAGE = "요청 데이터가 잘못되었습니다.";

	@NotNull(message = INVALID_DATA_MESSAGE)
	private ChatType type;

	@NotNull(message = INVALID_DATA_MESSAGE)
	private ChatRole role;

	@NotNull(message = INVALID_DATA_MESSAGE)
	private String message;

	@NotNull(message = INVALID_DATA_MESSAGE)
	private LocalDateTime timestamp;
}
