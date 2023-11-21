package efub.back.jupjup.domain.postjoin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.postjoin.domain.Postjoin;

public interface PostjoinRepository extends JpaRepository<Postjoin, Long> {
	Optional<Postjoin> findByMemberAndPost(Member member, Post post);

	Boolean existsByMemberAndPost(Member member, Post post);

	List<Postjoin> findAllByPost(Post post);
	List<Postjoin> findByMember(Member member);

	Long countByPost(Post post);

	long countByMember(Member member);

	@Query("SELECT p.member.id FROM Postjoin p WHERE p.post.Id = :postId")
	List<Long> findMemberIdsByPostId(@Param("postId") Long postId);
}
