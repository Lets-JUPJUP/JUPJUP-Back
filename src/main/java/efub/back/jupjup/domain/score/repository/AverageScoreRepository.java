package efub.back.jupjup.domain.score.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.score.domain.AverageScore;

public interface AverageScoreRepository extends JpaRepository<AverageScore, Long> {
	Optional<AverageScore> findByMemberId(Long memberId);
}
