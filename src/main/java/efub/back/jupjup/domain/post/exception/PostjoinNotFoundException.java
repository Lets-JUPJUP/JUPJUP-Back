package efub.back.jupjup.domain.post.exception;

import efub.back.jupjup.global.exception.custom.NotFoundException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostjoinNotFoundException extends NotFoundException {
	public PostjoinNotFoundException(Long id) {
		super("id = " + id);
	}

	public PostjoinNotFoundException(Long id1, Long id2) {
		super("id = " + id1 + " & " + id2);
	}
}
