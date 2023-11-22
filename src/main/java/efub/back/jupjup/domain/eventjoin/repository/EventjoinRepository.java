package efub.back.jupjup.domain.eventjoin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.eventInfo.domain.EventInfo;
import efub.back.jupjup.domain.eventjoin.domain.Eventjoin;
import efub.back.jupjup.domain.member.domain.Member;

public interface EventjoinRepository extends JpaRepository<Eventjoin, Long> {
	Long countByEventInfo(EventInfo eventInfo);
	Optional<Eventjoin> findByMemberAndEventInfo(Member member, EventInfo eventInfo);
}
