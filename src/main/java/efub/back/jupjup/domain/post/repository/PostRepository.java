package efub.back.jupjup.domain.post.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostGender;
import efub.back.jupjup.domain.post.dto.PostFilterDto;
import lombok.RequiredArgsConstructor;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
	List<Post> findAllByPostGender(PostGender postGender, Sort sort);
	List<Post> findAllByWithPet(boolean withPet, Sort sort);
	long countByAuthor(Member author);
	List<Post> findAllByAuthor(Member author, Sort sort);
	List<Post> findByDueDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
	List<Post> findAllByDueDateAfterOrderByCreatedAtDesc(LocalDateTime now);
	List<Post> findAllByDueDateBeforeAndIsRecruitmentSuccessfulTrueOrderByCreatedAtDesc(LocalDateTime now);
	List<Post> findAllByDueDateBeforeOrderByCreatedAtDesc(LocalDateTime now);
	List<Post> findAllByDueDateBeforeOrderByDueDateDesc(LocalDateTime now);
}
