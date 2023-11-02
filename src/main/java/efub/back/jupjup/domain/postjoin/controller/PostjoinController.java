package efub.back.jupjup.domain.postjoin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.postjoin.service.PostjoinService;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostjoinController {
	private final PostjoinService postjoinService;
	private final PostRepository postRepository;

	// 게시글에 참여
	@PostMapping("/{postId}/join")
	public ResponseEntity<StatusResponse> joinPost(@PathVariable Long postId, @AuthUser Member member) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
		return postjoinService.joinPost(member, postId);
	}

	// 게시글 참여 취소
	@DeleteMapping("/{postId}/unjoin")
	public ResponseEntity<StatusResponse> unjoinPost(@PathVariable Long postId, @AuthUser Member member) {
		return postjoinService.unjoinPost(member, postId);
	}

	// 게시글에 참여한 멤버 조회
	@GetMapping("/{postId}/members")
	public ResponseEntity<StatusResponse> getJoinedMembers(@PathVariable Long postId) {
		return postjoinService.getJoinedMembers(postId);
	}
}
