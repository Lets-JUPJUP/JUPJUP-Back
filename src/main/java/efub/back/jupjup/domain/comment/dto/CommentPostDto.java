package efub.back.jupjup.domain.comment.dto;

import java.time.LocalDateTime;
import efub.back.jupjup.domain.comment.domain.Comment;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostGender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentPostDto {
	private Long id;
	private String title;
	private String startPlace;
	private LocalDateTime startDate;
	private PostGender postGender;
	private boolean withPet;
	private LocalDateTime createdDate;

	public static CommentPostDto of(Comment comment) {
		Post post = comment.getPost();
		return CommentPostDto.builder()
			.id(comment.getPost().getId())
			.title(comment.getPost().getTitle())
			.startDate(comment.getPost().getStartDate())
			.postGender(comment.getPost().getPostGender())
			.withPet(comment.getPost().isWithPet())
			.createdDate(comment.getPost().getCreatedAt())
			.build();
	}
}
