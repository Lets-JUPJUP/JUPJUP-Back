package efub.back.jupjup.domain.postjoin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.postjoin.domain.Postjoin;

public interface PostjoinRepository extends JpaRepository<Postjoin, Long> {
	Optional<Postjoin> findByMemberAndPost(Member member, Post post);
	Boolean existsByMemberAndPost(Member member, Post post);
}
