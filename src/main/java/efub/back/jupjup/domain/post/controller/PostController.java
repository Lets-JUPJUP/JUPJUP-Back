package efub.back.jupjup.domain.post.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.District;
import efub.back.jupjup.domain.post.dto.PostFilterDto;
import efub.back.jupjup.domain.post.dto.PostRequestDto;
import efub.back.jupjup.domain.post.service.PostService;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
	private final PostService postService;

	// 게시글 작성
	@PostMapping
	public ResponseEntity<StatusResponse> savePost(@RequestBody PostRequestDto requestDto,
		@AuthUser Member member) {
		return postService.savePost(requestDto, member);
	}

	// 전체 게시글 조회 : 1개
	@GetMapping("/{postId}")
	public ResponseEntity<StatusResponse> getPost(@PathVariable Long postId, @AuthUser Member member){
		return postService.getPost(postId, member);
	}

	// 전체 게시글 리스트 조회
	@GetMapping("/list")
	public ResponseEntity<StatusResponse> getPosts(@AuthUser Member member) {
		return postService.getAllPosts(member);
	}

	// 성별 필터링 게시글 리스트 조회
	@GetMapping("/listByGender")
	public ResponseEntity<StatusResponse> getPostsByGender(@RequestParam String postGender, @AuthUser Member member) {
		return postService.getPostsByGender(postGender, member);
	}

	// 반려동물 여부 필터링 게시글 리스트 조회
	@GetMapping("/listByPet")
	public ResponseEntity<StatusResponse> getPostsByWithPet(@RequestParam boolean withPet, @AuthUser Member member) {
		return postService.getPostsByWithPet(withPet, member);
	}

	// 게시글 삭제
	@DeleteMapping("/{postId}")
	public ResponseEntity<StatusResponse> deletePost(@AuthUser Member member, @PathVariable Long postId) {
		return postService.deletePost(member, postId);
	}

	// 전체 게시글 개수와 참여한 게시글 개수 조회
	@GetMapping("/counts")
	public ResponseEntity<StatusResponse> getPostCounts(@AuthUser Member member) {
		return postService.getPostCounts(member);
	}

	// 특정 사용자의 주최한 플로깅 개수와 참여한 플로깅 개수 조회
	@GetMapping("/{memberId}/counts")
	public ResponseEntity<StatusResponse> getUserPostCounts(@PathVariable Long memberId) {
		return postService.getUserPostCounts(memberId);
	}

	// 로그인한 사용자가 주최한 게시글 리스트 조회
	@GetMapping("/hosted")
	public ResponseEntity<StatusResponse> getHostedPosts(@AuthUser Member member) {
		return postService.getHostedPosts(member);
	}

	// 로그인한 사용자가 참여한 게시글 리스트 조회
	@GetMapping("/joined")
	public ResponseEntity<StatusResponse> getJoinedPosts(@AuthUser Member member) {
		return postService.getJoinedPosts(member);
	}

	// [모집 중] 게시글 리스트 조회
	@GetMapping("/recruiting")
	public ResponseEntity<StatusResponse> getRecruitingPosts(@AuthUser Member member) {
		return postService.getRecruitingPosts(member);
	}

	// [모집 완료] 게시글 리스트 조회
	@GetMapping("/successful")
	public ResponseEntity<StatusResponse> getSuccessfulRecruitmentPosts(@AuthUser Member member) {
		return postService.getSuccessfulRecruitmentPosts(member);
	}

	// [완료] 게시글 리스트 조회
	@GetMapping("/completed")
	public ResponseEntity<StatusResponse> getCompletedPosts(@AuthUser Member member) {
		return postService.getCompletedPosts(member);
	}

	// 필터링된 게시글 리스트 조회
	@GetMapping("/filter")
	public ResponseEntity<StatusResponse> getFilteredPosts(
		@ModelAttribute PostFilterDto filterDto,
		@AuthUser Member member,
		@RequestParam(required = false) List<String> districts) {

		if (districts != null && !districts.isEmpty()) {
			List<District> districtList = districts.stream()
				.map(District::from)
				.collect(Collectors.toList());
			filterDto.setDistricts(districtList);
		}

		filterDto.setUserGender(member.getGender());
		return postService.getFilteredPosts(filterDto, member);
	}

	// 가장 최근에 완료한 플로깅 게시글 1개 조회하기
	@GetMapping("/latest-completed")
	public ResponseEntity<StatusResponse> getLatestCompletedPost(@AuthUser Member member) {
		return postService.getLatestCompletedPost(member);
	}
}
