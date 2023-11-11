package efub.back.jupjup.domain.heart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.heart.service.HeartService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hearts")
@RequiredArgsConstructor
public class HeartController {
	private final HeartService heartService;
	private final PostRepository postRepository;

	// 게시글 찜하기
	@PostMapping("/{postId}")
	public ResponseEntity<StatusResponse> heartPost(@PathVariable Long postId, @AuthUser Member member) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
		return heartService.heartPost(member, postId);
	}

	// 게시글 찜하기 취소
	@DeleteMapping("/{postId}")
	public ResponseEntity<StatusResponse> unheartPost(@PathVariable Long postId, @AuthUser Member member) {
		return heartService.unheartPost(member, postId);
	}

	// 찜한 글 모아보기
	@GetMapping("/lists")
	public ResponseEntity<StatusResponse> findHeartPostList(@AuthUser Member member) {
		return heartService.findHeartPostList(member);
	}
}
