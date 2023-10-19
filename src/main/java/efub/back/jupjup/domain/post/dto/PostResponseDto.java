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
@AllArgsConstructor
@Builder
public class PostResponseDto {
	private Long postId;
	private String title;
	private String hostnickname;
	private String content;
	private String startPlace;
	private LocalDateTime startDate;
	private int minMember;
	private int maxMember;
	private PostGender postGender;
	private List<PostAgeRange> postAgeRanges;
	private LocalDateTime dueDate;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private List<String> imageUrls;

	public PostResponseDto (Post post, Member member) {

		this.postId = post.getPostId();
		this.title = post.getTitle();
		this.hostnickname = member.getNickname();
		this.content = post.getContent();
		this.startPlace = post.getStartPlace();
		this.startDate = post.getStartDate();
		this.minMember = post.getMinMember();
		this.maxMember = post.getMaxMember();
		this.postGender = post.getPostGender();
		this.postAgeRanges = post.getPostAgeRanges();
		this.dueDate = post.getDueDate();
		this.createdAt = post.getCreatedAt();
		this.modifiedAt = post.getModifiedAt();
	}
}
