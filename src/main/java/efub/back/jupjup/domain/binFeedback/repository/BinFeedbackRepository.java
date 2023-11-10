package efub.back.jupjup.domain.binFeedback.repository;

import efub.back.jupjup.domain.binFeedback.domain.BinFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinFeedbackRepository extends JpaRepository<BinFeedback, Long> {
}
