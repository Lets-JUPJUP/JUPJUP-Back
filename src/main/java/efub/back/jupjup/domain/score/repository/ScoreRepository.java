package efub.back.jupjup.domain.score.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.score.domain.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {

}
