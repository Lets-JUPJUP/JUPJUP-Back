package efub.back.jupjup.domain.comment.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessagingException;

import efub.back.jupjup.domain.comment.domain.Comment;
import efub.back.jupjup.domain.comment.dto.CommentDto;
import efub.back.jupjup.domain.comment.dto.CommentPostDto;
import efub.back.jupjup.domain.comment.dto.CommentRequestDto;
import efub.back.jupjup.domain.comment.dto.CommentResponseDto;
import efub.back.jupjup.domain.comment.dto.ReplyRequestDto;
import efub.back.jupjup.domain.comment.dto.ReplyResponseDto;
import efub.back.jupjup.domain.comment.exception.NoAuthorityCommentRemoveException;
import efub.back.jupjup.domain.comment.exception.NoCommentExistsException;
import efub.back.jupjup.domain.comment.exception.NoPostExistException;
import efub.back.jupjup.domain.comment.repository.CommentRepository;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.notification.domain.NotificationType;
import efub.back.jupjup.domain.notification.service.FirebaseService;
import efub.back.jupjup.domain.notification.service.NotificationService;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final NotificationService notificationService;
	private final FirebaseService firebaseService;

	// 댓글 생성 기능
	public ResponseEntity<StatusResponse> saveComment(Long postId, CommentRequestDto commentReqDto,
		Member member) throws
		FirebaseMessagingException {
		Post post = postRepository.findById(postId).orElseThrow(NoPostExistException::new);
		Comment comment = Comment.builder()
			.content(commentReqDto.getContent())
			.writer(member)
			.post(post)
			.build();
		commentRepository.save(comment);

		if (!post.getAuthor().getId().equals(member.getId())) {
			notificationService.send(post.getAuthor(), NotificationType.COMMENT, comment.getContent(),
				comment.getPost().getId());
			firebaseService.sendPushMessage(post.getAuthor().getId(),
				"댓글 알림",
				String.format("%s님이 댓글을 달았습니다.", member.getNickname()));

		}

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(CommentDto.of(comment, post, member, null))
			.build());
	}

	// 대댓글 생성 기능
	public ResponseEntity<StatusResponse> saveReply(Long postId, ReplyRequestDto replyReqDto, Member member) throws
		FirebaseMessagingException {
		Post post = postRepository.findById(postId).orElseThrow(() -> new NoPostExistException());
		Comment parent = commentRepository.findById(replyReqDto.getParentId())
			.orElseThrow(() -> new NoCommentExistsException());
		Comment comment = Comment.builder()
			.content(replyReqDto.getContent())
			.writer(member)
			.post(post)
			.build();
		comment.setParent(parent);
		commentRepository.save(comment);

		if (!parent.getWriter().getId().equals(member.getId())) {
			notificationService.send(parent.getWriter(), NotificationType.REPLY, replyReqDto.getContent(),
				comment.getPost().getId());
			firebaseService.sendPushMessage(parent.getWriter().getId(),
				"대댓글 알림",
				String.format("%s님이 댓글을 달았습니다.", member.getNickname()));
		}

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(CommentDto.of(comment, post, member, null))
			.build());
	}

	// 댓글 조회 기능
	public ResponseEntity<StatusResponse> getCommentList(Long postId) {
		Post findPost = postRepository.findById(postId).orElseThrow(NoPostExistException::new);
		List<Comment> commentList = commentRepository.findAllByPostAndParentIsNull(findPost);
		Integer commentNo = commentRepository.countAllByPost(findPost);
		List<CommentDto> commentDtoList = new ArrayList<>();
		for (Comment comment : commentList) {
			Member member = memberRepository.findById(comment.getWriter().getId()).get();
			List<Comment> childList = comment.getChildList();
			List<ReplyResponseDto> replyList = childList.stream()
				.map(child -> ReplyResponseDto.of(child, findPost, member))
				.collect(Collectors.toList());
			CommentDto commentDto = CommentDto.of(comment, findPost, member, replyList);
			commentDtoList.add(commentDto);
		}

		CommentResponseDto responseDto = CommentResponseDto.builder()
			.commentDtoList(commentDtoList)
			.commentNo(commentNo)
			.build();

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(responseDto)
			.build());
	}

	// 댓글 삭제하기
	public ResponseEntity<StatusResponse> removeComment(Long commentId, Member member) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoCommentExistsException());

		Long writerId = comment.getWriter().getId();
		if (!writerId.equals(member.getId())) {
			throw new NoAuthorityCommentRemoveException();
		}
		comment.remove();
		List<Comment> commentListToDelete = comment.findCommentListToDelete();
		commentRepository.deleteAll(commentListToDelete);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.build());

	}

	// 대댓글 삭제하기
	public ResponseEntity<StatusResponse> removeReply(Long replyId, Member member) {
		Comment reply = commentRepository.findById(replyId)
			.orElseThrow(() -> new NoCommentExistsException());

		Long writerId = reply.getWriter().getId();
		if (!writerId.equals(member.getId())) {
			throw new NoAuthorityCommentRemoveException();
		}

		commentRepository.delete(reply);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data("대댓글이 삭제되었습니다.")
			.build());
	}

	// 내가 쓴 댓글의 게시글 모아보기
	public ResponseEntity<StatusResponse> getCommentedPosts(Member member) {
		List<Comment> comments = commentRepository.findByWriter(member);

		List<CommentPostDto> commentedPosts = comments.stream()
			.collect(Collectors.toMap(
				comment -> comment.getPost().getId(),
				CommentPostDto::of,
				(existing, replacement) -> existing,
				LinkedHashMap::new
			))
			.values().stream()
			.collect(Collectors.toList());

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(commentedPosts)
			.build());
	}
}
