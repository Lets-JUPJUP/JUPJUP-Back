package efub.back.jupjup.domain.trashCan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.trashCan.domain.BinFeedback;

public interface BinFeedbackRepository extends JpaRepository<BinFeedback, Long> {
	List<BinFeedback> findAllByMember(Member member);
}
