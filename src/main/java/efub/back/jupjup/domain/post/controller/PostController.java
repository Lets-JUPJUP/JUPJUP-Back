package efub.back.jupjup.domain.post.controller;

import efub.back.jupjup.domain.member.domain.Member;
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

	// 성별 필터링 게시글 리스트 조회
	@GetMapping("/listByGender")
	public ResponseEntity<StatusResponse> getPostsByGender(@RequestParam String postGender, @AuthUser Member member) {
		return postService.getPostsByGender(postGender, member);
	}

	// 게시글 삭제
	@DeleteMapping("/{postId}")
	public ResponseEntity<StatusResponse> deletePost(@AuthUser Member member, @PathVariable Long postId) {
		return postService.deletePost(member, postId);
	}

	@PostMapping("/images")
	public ResponseEntity<StatusResponse> getPresignedUrls(@AuthUser Member member,
		@RequestBody ImageUploadRequestDto imageUploadRequestDto) {
		return postService.getPresignedUrls(member, imageUploadRequestDto);
	}
}
