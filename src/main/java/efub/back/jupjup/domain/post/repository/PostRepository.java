package efub.back.jupjup.domain.post.repository;

import efub.back.jupjup.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
