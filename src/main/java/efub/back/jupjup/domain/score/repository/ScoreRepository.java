package efub.back.jupjup.domain.score.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.score.domain.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {
	Optional<Score> findByParticipantAndPost(Member participant, Post post);

	// 리뷰 완료 여부
	Boolean existsByParticipantAndPost(Member participant, Post post);

}
