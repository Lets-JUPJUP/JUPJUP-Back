package efub.back.jupjup.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostAgeRange;
import efub.back.jupjup.domain.post.domain.PostGender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequestDto {
	private String title;
	private String content;
	private String startPlace;
	private LocalDateTime startDate;
	private LocalDateTime dueDate;
	private int minMember;
	private int maxMember;
	private PostGender postGender;
	private List<PostAgeRange> postAgeRanges;
	private boolean withPet;
	private List<String> images;

	public Post toEntity(PostRequestDto dto, Member member) {
		return Post.builder()
			.title(dto.getTitle())
			.content(dto.getContent())
			.startPlace(dto.getStartPlace())
			.startDate(dto.getStartDate())
			.minMember(dto.getMinMember())
			.maxMember(dto.getMaxMember())
			.postGender(dto.getPostGender())
			.postAgeRanges(dto.getPostAgeRanges())
			.dueDate(dto.getDueDate())
			.withPet(dto.isWithPet())
			.author(member)
			.isRecruitmentSuccessful(false)
			.build();
	}

}
