package efub.back.jupjup.domain.postjoin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import efub.back.jupjup.domain.postjoin.domain.Postjoin;

public interface PostjoinRepository extends JpaRepository<Postjoin, Long> {
	Optional<Postjoin> findByMemberIdAndPostId(Long memberId, Long postId);
	Boolean existsPostjoinByMemberIdAndPostId(Long memberId, Long postId);
}
