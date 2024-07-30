package efub.back.jupjup.domain.post.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.District;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostGender;
import efub.back.jupjup.domain.post.domain.Route;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequestDto {
	private String title;
	private LocalDateTime startDate;
	private LocalDateTime dueDate;
	private int minAge;
	private int maxAge;
	private int minMember;
	private int maxMember;
	private PostGender postGender;
	private boolean withPet;
	private String content;
	private List<String> images;
	private String district;
	private List<RouteDto> route;

	@Getter
	@NoArgsConstructor
	public static class RouteDto {
		private String address;
		private double latitude;
		private double longitude;
	}

	public Post toEntity(PostRequestDto dto, Member member) {
		return Post.builder()
			.title(dto.getTitle())
			.content(dto.getContent())
			.startDate(dto.getStartDate())
			.dueDate(dto.getDueDate())
			.minAge(dto.getMinAge())
			.maxAge(dto.getMaxAge())
			.minMember(dto.getMinMember())
			.maxMember(dto.getMaxMember())
			.postGender(dto.getPostGender())
			.withPet(dto.isWithPet())
			.district(District.valueOf(dto.getDistrict()))
			.route(dto.getRoute().stream()
				.map(routeDto -> new Route(routeDto.getAddress(), routeDto.getLatitude(), routeDto.getLongitude()))
				.collect(Collectors.toList()))
			.author(member)
			.isRecruitmentSuccessful(false)
			.build();
	}
}
