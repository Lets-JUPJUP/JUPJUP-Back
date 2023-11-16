package efub.back.jupjup.domain.notification;

import java.util.Map;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {
	SseEmitter save(String emitterId, SseEmitter sseEmitter);

	void deleteById(String emitterId);

	Map<String, Object> findAllEventCacheStartWithMemberId(String receiverId);

	Map<String, SseEmitter> findAllEmittersStartWithMemberId(String receiverId);

	void saveEventCache(String emitterId, Object object);
}
