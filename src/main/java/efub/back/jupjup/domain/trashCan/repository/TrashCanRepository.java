package efub.back.jupjup.domain.trashCan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import efub.back.jupjup.domain.trashCan.domain.TrashCan;

public interface TrashCanRepository extends JpaRepository<TrashCan, Long> {
	@Query(value = "SELECT * FROM trash_can WHERE latitude BETWEEN ?1 AND ?2 AND longitude BETWEEN ?3 AND ?4", nativeQuery = true)
	List<TrashCan> findWithinMap(Double startX, Double endX, Double startY, Double endY);

	boolean existsById(Long id);
}
