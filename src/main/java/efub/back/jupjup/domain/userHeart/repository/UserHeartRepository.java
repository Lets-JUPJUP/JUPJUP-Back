package efub.back.jupjup.domain.userHeart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import efub.back.jupjup.domain.userHeart.domain.UserHeart;

public interface UserHeartRepository extends JpaRepository<UserHeart, Long> {
	@Query("SELECT COUNT(u) FROM UserHeart u WHERE u.id = :memberId")
	Long countUserHeartsByMemberId(@Param("memberId") Long memberId);
}
