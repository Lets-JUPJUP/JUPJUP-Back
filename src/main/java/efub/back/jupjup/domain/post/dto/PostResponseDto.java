package efub.back.jupjup.domain.post.dto;

import efub.back.jupjup.domain.post.domain.Gender;
import efub.back.jupjup.domain.post.domain.AgeRange;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
	private Gender gender;
	private AgeRange ageRange;
	private LocalDateTime dueDate;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	// Image 관련 필드 추가해야함

	public PostResponseDto(Post post, Member member) {
		this.postId = post.getPostId();
		this.title = post.getTitle();
		this.hostnickname = member.getNickname();
		this.content = post.getContent();
		this.startPlace = post.getStartPlace();
		this.startDate = post.getStartDate();
		this.minMember = post.getMinMember();
		this.maxMember = post.getMaxMember();
		this.gender = post.getGender();
		this.ageRange = post.getAgeRange();
		this.dueDate = post.getDueDate();
		this.createdAt=post.getCreatedAt();
		this.modifiedAt=post.getModifiedAt();
	}
}
