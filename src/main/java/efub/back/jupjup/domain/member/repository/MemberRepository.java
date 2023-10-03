package efub.back.jupjup.domain.member.repository;

import efub.back.jupjup.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<Member> findByUsername(String username);

    boolean existsByNickname(String nickname);
}
