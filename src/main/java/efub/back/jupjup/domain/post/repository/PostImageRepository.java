package efub.back.jupjup.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, String> {
	List<PostImage> findAllByPost(Post post);
	void deleteAllByPost(Post post);
}
