package efub.back.jupjup.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import efub.back.jupjup.domain.report.domain.ReportImage;

public interface ReportImageRepository extends JpaRepository<ReportImage, String> {
}
