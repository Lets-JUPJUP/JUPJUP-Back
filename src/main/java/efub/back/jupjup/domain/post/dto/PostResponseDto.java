package efub.back.jupjup.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import efub.back.jupjup.domain.post.domain.District;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostGender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponseDto {
	private Long id;
	private String title;
	private LocalDateTime startDate;
	private LocalDateTime dueDate;
	private int minAge;
	private int maxAge;
	private int minMember;
	private int maxMember;
	private PostGender postGender;
	private Boolean withPet;
	private String content;
	private LocalDateTime createdAt;
	private List<String> fileUrls;
	private List<RouteResponseDto> route;
	private District district;
	private Long authorId;
	private Optional<Boolean> isJoined; // 참여 여부를 표시하는 필드
	private Optional<Boolean> isHearted; // 찜하기 여부를 표시하는 필드
	private Boolean isEnded;  // 모집 마감 여부를 표시하는 필드
	private Boolean isAuthor; // 작성자인지 여부를 표시하는 필드
	private String authorNickname;           // 글쓴이의 닉네임 필드
	private String authorProfileImageUrl;    // 글쓴이의 프로필 이미지 URL 필드
	private Boolean isRecruitmentSuccessful;

	@Getter
	@Builder
	public static class RouteResponseDto {
		private String address;
		private double latitude;
		private double longitude;
	}

	public static PostResponseDto of(Post post, List<String> imgUrlList, Optional<Boolean> isJoined, Optional<Boolean> isHearted, Boolean isEnded, Boolean isAuthor) {
		return PostResponseDto.builder()
			.id(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.startDate(post.getStartDate())
			.dueDate(post.getDueDate())
			.minAge(post.getMinAge())
			.maxAge(post.getMaxAge())
			.minMember(post.getMinMember())
			.maxMember(post.getMaxMember())
			.postGender(post.getPostGender())
			.withPet(post.isWithPet())
			.content(post.getContent())
			.createdAt(post.getCreatedAt())
			.fileUrls(imgUrlList)
			.route(post.getRoute().stream()
				.map(route -> RouteResponseDto.builder()
					.address(route.getAddress())
					.latitude(route.getLatitude())
					.longitude(route.getLongitude())
					.build())
				.collect(Collectors.toList()))
			.district(post.getDistrict())
			.authorId(post.getAuthor().getId())
			.isJoined(isJoined)
			.isHearted(isHearted)
			.isEnded(isEnded)
			.isAuthor(isAuthor)
			.authorNickname(post.getAuthor().getNickname())
			.authorProfileImageUrl(post.getAuthor().getProfileImageUrl())
			.isRecruitmentSuccessful(post.getIsRecruitmentSuccessful())
			.build();
	}
}
