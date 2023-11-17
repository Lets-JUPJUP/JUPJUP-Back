package efub.back.jupjup.domain.post.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostAgeRange;
import efub.back.jupjup.domain.post.domain.PostGender;

public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findAllByPostGender(PostGender postGender);

	List<Post> findAllByPostAgeRangesContaining(PostAgeRange postAgeRange);

	List<Post> findAllByWithPet(boolean withPet);

	long countByAuthor(Member author);

	List<Post> findByDueDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
