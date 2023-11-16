package efub.back.jupjup.domain.post.controller;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.PostAgeRange;
import efub.back.jupjup.domain.post.dto.ImageUploadRequestDto;
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

	// 전체 게시글 리스트 조회 - (로그인 없이)
	@GetMapping("/list/unauth")
	public ResponseEntity<StatusResponse> getPosts() {
		return postService.getAllPostsUnAuth();
	}

	// 성별 필터링 게시글 리스트 조회
	@GetMapping("/listByGender")
	public ResponseEntity<StatusResponse> getPostsByGender(@RequestParam String postGender, @AuthUser Member member) {
		return postService.getPostsByGender(postGender, member);
	}

	// 성별 필터링 게시글 리스트 조회 - 로그인 없이
	@GetMapping("/listByGender/unauth")
	public ResponseEntity<StatusResponse> getPostsByGender(@RequestParam String postGender) {
		return postService.getPostsByGenderUnAuth(postGender);
	}

	// 나이대 필터링 게시글 리스트 조회
	@GetMapping("/listByAgeRange")
	public ResponseEntity<StatusResponse> getPostsByAgeRange(@RequestParam String postAgeRangeStr, @AuthUser Member member) {
		PostAgeRange postAgeRange = PostAgeRange.fromString(postAgeRangeStr);
		return postService.getPostsByAgeRange(postAgeRange, member);
	}

	// 나이대 필터링 게시글 리스트 조회 - 로그인 없이
	@GetMapping("/listByAgeRange/unauth")
	public ResponseEntity<StatusResponse> getPostsByAgeRange(@RequestParam String postAgeRangeStr) {
		PostAgeRange postAgeRange = PostAgeRange.fromString(postAgeRangeStr);
		return postService.getPostsByAgeRangeUnAuth(postAgeRange);
	}

	// 반려동물 여부 필터링 게시글 리스트 조회
	@GetMapping("/listByPet")
	public ResponseEntity<StatusResponse> getPostsByWithPet(@RequestParam boolean withPet, @AuthUser Member member) {
		return postService.getPostsByWithPet(withPet, member);
	}

	// 반려동물 여부 필터링 게시글 리스트 조회 - 로그인 없이
	@GetMapping("/listByPet/unauth")
	public ResponseEntity<StatusResponse> getPostsByWithPet(@RequestParam boolean withPet) {
		return postService.getPostsByWithPetUnAuth(withPet);
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
}
