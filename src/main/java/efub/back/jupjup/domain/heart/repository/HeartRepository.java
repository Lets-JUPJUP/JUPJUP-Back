package efub.back.jupjup.domain.heart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.heart.domain.Heart;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;

public interface HeartRepository extends JpaRepository<Heart, Long> {
	Optional<Heart> findByMemberAndPost(Member member, Post post);
	List<Heart> findAllByMemberOrderByIdDesc(Member member);
	Long countByMember(Member member);
	boolean existsByMemberAndPost(Member member, Post post);
}
