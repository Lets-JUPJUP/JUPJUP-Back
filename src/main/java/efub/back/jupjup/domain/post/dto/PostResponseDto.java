package efub.back.jupjup.domain.post.dto;

import efub.back.jupjup.domain.post.domain.PostGender;
import efub.back.jupjup.domain.post.domain.PostAgeRange;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostImage;
import efub.back.jupjup.domain.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostResponseDto {
	private Long id;
	private String title;
	private String content;
	private String startPlace;
	private LocalDateTime startDate;
	private int minMember;
	private int maxMember;
	private PostGender postGender;
	private List<PostAgeRange> postAgeRanges;
	private LocalDateTime dueDate;
	private LocalDateTime createdAt;
	private List<String> fileUrls;
	private Long authorId;

	public static PostResponseDto of(Post post, List<String> imgUrlList) {
		return PostResponseDto.builder()
			.id(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.startPlace(post.getStartPlace())
			.startDate(post.getStartDate())
			.minMember(post.getMinMember())
			.maxMember(post.getMaxMember())
			.postGender(post.getPostGender())
			.postAgeRanges(post.getPostAgeRanges())
			.dueDate(post.getDueDate())
			.createdAt(post.getCreatedAt())
			.fileUrls(imgUrlList)
			.authorId(post.getAuthor().getId())
			.build();
	}
}
