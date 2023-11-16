package efub.back.jupjup.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostAgeRange;
import efub.back.jupjup.domain.post.domain.PostGender;
import lombok.Builder;
import lombok.Getter;

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
	private Boolean withPet;
	private Long authorId;
	private Optional<Boolean> isJoined; // 참여 여부를 표시하는 필드
	private Optional<Boolean> isHearted; // 찜하기 여부를 표시하는 필드
	private Boolean isEnded;  // 모집 마감 여부를 표시하는 필드
	private String authorNickname;           // 글쓴이의 닉네임 필드
	private String authorProfileImageUrl;    // 글쓴이의 프로필 이미지 URL 필드
	private Boolean isRecruitmentSuccessful;

	public static PostResponseDto of(Post post, List<String> imgUrlList, Optional<Boolean> isJoined, Optional<Boolean> isHearted, Boolean isEnded) {

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
			.withPet(post.isWithPet())
			.authorId(post.getAuthor().getId())
			.isHearted(isHearted)
			.isJoined(isJoined)
			.isEnded(isEnded)
			.authorNickname(post.getAuthor().getNickname())
			.authorProfileImageUrl(post.getAuthor().getProfileImageUrl())
			.isRecruitmentSuccessful(post.getIsRecruitmentSuccessful())
			.build();
	}
}
