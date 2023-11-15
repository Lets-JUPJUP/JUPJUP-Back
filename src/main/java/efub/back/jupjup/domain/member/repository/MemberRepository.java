package efub.back.jupjup.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Optional<Member> findByNickname(String nickname);

	boolean existsByEmail(String email);

	Optional<Member> findByUsername(String username);

	boolean existsByNickname(String nickname);

	Long countByNickname(String nickname);
}
