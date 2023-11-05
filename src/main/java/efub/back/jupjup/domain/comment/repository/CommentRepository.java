package efub.back.jupjup.domain.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.comment.dto.CommentPostDto;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByPost(Post post);
	List<Comment> findAllByWriterOrderByCreatedAtDesc(Member writer);
	Integer countAllByPost(Post post);
	List<Comment> findByWriter(Member writer);

}
