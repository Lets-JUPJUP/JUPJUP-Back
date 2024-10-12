package efub.back.jupjup.domain.chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import efub.back.jupjup.domain.chat.domain.Chat;

public interface ChatRepository extends MongoRepository<Chat, String> {
	List<Chat> findByUserIdOrderByTimestampAsc(Long userId);

	void deleteByUserId(Long userId);
}
