package efub.back.jupjup.domain.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.comment.dto.CommentRequestDto;
import efub.back.jupjup.domain.comment.dto.ReplyRequestDto;
import efub.back.jupjup.domain.comment.service.CommentService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	// 댓글 생성
	@PostMapping("/{postId}")
	public ResponseEntity<StatusResponse> createComment(@PathVariable Long postId,
		@RequestBody CommentRequestDto commentReqDto,
		@AuthUser Member member) {
		return commentService.saveComment(postId, commentReqDto, member);
	}

	// 대댓글 생성
	@PostMapping("/reply/{postId}")
	public ResponseEntity<StatusResponse> createReply(@PathVariable Long postId,
		@RequestBody ReplyRequestDto replyRequestDto,
		@AuthUser Member member) {
		return commentService.saveReply(postId, replyRequestDto, member);
	}

	// 댓글 목록 조회
	@GetMapping("/{postId}")
	public ResponseEntity<StatusResponse> getComments(@PathVariable Long postId) {
		return commentService.getCommentList(postId);
	}

	// 댓글 삭제
	@DeleteMapping("/{commentId}")
	public ResponseEntity<StatusResponse> deleteComment(@PathVariable Long commentId,
		@AuthUser Member member) {
		return commentService.removeComment(commentId, member);
	}

	// 사용자가 작성한 댓글이 달린 게시글 조회
	@GetMapping("/commented-posts")
	public ResponseEntity<StatusResponse> getCommentedPosts(@AuthUser Member member) {
		return commentService.getCommentedPosts(member);
	}
}
