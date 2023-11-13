package efub.back.jupjup.domain.eventcomment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.eventcomment.domain.Eventcomment;

public interface EventcommentRepository extends JpaRepository<Eventcomment, Long> {
	List<Eventcomment> findAllByEventInfoId(Long eventInfoId);
}
