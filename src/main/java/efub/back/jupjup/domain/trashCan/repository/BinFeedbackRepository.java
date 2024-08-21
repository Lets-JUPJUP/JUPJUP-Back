package efub.back.jupjup.domain.trashCan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.trashCan.domain.BinFeedback;
import efub.back.jupjup.domain.trashCan.domain.Feedback;

public interface BinFeedbackRepository extends JpaRepository<BinFeedback, Long> {
	Optional<BinFeedback> findBinFeedbackByMemberAndTrashCanId(Member member, Long trashCanId);

	Boolean existsBinFeedbackByMemberAndTrashCanId(Member member, Long trashCanId);

	List<BinFeedback> findAllByTrashCanId(Long trashCanId);

	@Query("SELECT COUNT(bf) FROM BinFeedback bf WHERE bf.feedback = :feedback AND bf.trashCanId = :trashCanId")
	Long countByFeedbackAndTrashCanId(@Param("feedback") Feedback feedback, @Param("trashCanId") Long trashCanId);

	// 쓰레기통별 피드백 개수 조회 메서드
	@Query("SELECT bf.trashCanId, COUNT(bf) FROM BinFeedback bf WHERE bf.trashCanId IN (:trashCanIds) GROUP BY bf.trashCanId")
	List<Object[]> countFeedbacksByTrashCan(List<Long> trashCanIds);

	Long countByTrashCanId(Long trashCanId);
}
