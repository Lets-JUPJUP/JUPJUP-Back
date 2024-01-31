package efub.back.jupjup.domain.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import efub.back.jupjup.domain.report.domain.Report;
import efub.back.jupjup.domain.report.domain.ReportImage;

public interface ReportImageRepository extends JpaRepository<ReportImage, String> {
	List<ReportImage> findAllByReport(Report report);
}
