package efub.back.jupjup.global.redis;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import efub.back.jupjup.domain.score.domain.Badge;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisReviewCountService {
	private final RedisTemplate<String, String> redisTemplate;

	public void incrementReviewCountForMemberByBadge(Long memberId, Badge badge) {
		String key = "member:" + String.valueOf(memberId) + ":badge";
		redisTemplate.opsForZSet().incrementScore(key, badge.getCode(), 1);
	}

	public Set<TypedTuple<String>> getTop3BadgesForMember(Long memberId) {
		String key = "member:" + memberId + ":badge";
		return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 2); // 상위 3개의 배지 가져오기
	}

}
