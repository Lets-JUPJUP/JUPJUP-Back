package efub.back.jupjup.domain.report.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import efub.back.jupjup.domain.report.domain.Report;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
	Optional<Report> findById(Long reportId);
	@Override
	@EntityGraph(attributePaths = "reportImages")
	List<Report> findAll();
}
