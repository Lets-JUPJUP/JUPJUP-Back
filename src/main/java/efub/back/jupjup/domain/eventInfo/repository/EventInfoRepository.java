package efub.back.jupjup.domain.eventInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.eventInfo.domain.EventInfo;

public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {
}
