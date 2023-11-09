package efub.back.jupjup.domain.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment c WHERE c.post = :post AND c.parent IS NULL")
	List<Comment> findAllByPostAndParentIsNull(@Param("post") Post post);
	Integer countAllByPost(Post post);
	List<Comment> findByWriter(Member writer);
}
