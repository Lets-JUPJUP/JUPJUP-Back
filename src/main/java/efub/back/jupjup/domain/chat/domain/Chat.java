package efub.back.jupjup.domain.chat.domain;

import java.time.LocalDateTime;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@Document("chat")
public class Chat {
	@Id
	private String id;
	private Long userId;
	private ChatType type;
	private ChatRole role;
	private String message;
	@Indexed(name = "timestamp_index", expireAfterSeconds = 86400) // TODO : 604800로 변경(7일)
	private LocalDateTime timestamp;
}
