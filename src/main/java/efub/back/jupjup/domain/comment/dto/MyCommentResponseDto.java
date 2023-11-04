package efub.back.jupjup.domain.comment.dto;

import java.time.LocalDateTime;

import efub.back.jupjup.domain.comment.domain.Comment;
import efub.back.jupjup.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyCommentResponseDto {
	private Long id;
	private String postTitle;
	private LocalDateTime createdDate;

	public static MyCommentResponseDto of(Comment comment) {
		Post post = comment.getPost();
		return MyCommentResponseDto.builder()
			.id(comment.getPost().getId())
			.postTitle(post.getTitle())
			.createdDate(comment.getCreatedAt())
			.build();
	}
}
