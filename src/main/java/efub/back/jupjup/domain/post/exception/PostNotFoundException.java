package efub.back.jupjup.domain.post.exception;

import efub.back.jupjup.global.exception.custom.NotFoundException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostNotFoundException extends NotFoundException {
	public PostNotFoundException(Long postId) {
		super("contentId=" + postId);
	}
}
