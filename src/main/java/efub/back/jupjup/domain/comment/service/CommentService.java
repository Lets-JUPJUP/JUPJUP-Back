package efub.back.jupjup.domain.comment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.comment.domain.Comment;
import efub.back.jupjup.domain.comment.dto.CommentDto;
import efub.back.jupjup.domain.comment.dto.CommentPostDto;
import efub.back.jupjup.domain.comment.dto.CommentRequestDto;
import efub.back.jupjup.domain.comment.dto.CommentResponseDto;
import efub.back.jupjup.domain.comment.dto.MyCommentResponseDto;
import efub.back.jupjup.domain.comment.dto.ReplyRequestDto;
import efub.back.jupjup.domain.comment.dto.ReplyResponseDto;
import efub.back.jupjup.domain.comment.exception.NoAuthorityCommentRemoveException;
import efub.back.jupjup.domain.comment.repository.CommentRepository;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import efub.back.jupjup.domain.comment.exception.NoPostExistException;
import efub.back.jupjup.domain.comment.exception.NoCommentExistsException;;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	// 댓글 생성 기능
	public ResponseEntity<StatusResponse> saveComment(Long postId, CommentRequestDto commentReqDto, Member member) {
		Post post = postRepository.findById(postId).orElseThrow(NoPostExistException::new);
		Comment comment = Comment.builder()
			.content(commentReqDto.getContent())
			.writer(member)
			.post(post)
			.build();
		commentRepository.save(comment);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(CommentDto.of(comment, post, member, null))
			.build());
	}

	// 대댓글 생성 기능
	public ResponseEntity<StatusResponse> saveReply(Long postId, ReplyRequestDto replyReqDto, Member member) {
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

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(CommentDto.of(comment, post, member, null))
			.build());
	}

	// 댓글 조회 기능
	public ResponseEntity<StatusResponse> getCommentList(Long postId) {
		Post findPost = postRepository.findById(postId).orElseThrow(NoPostExistException::new);
		List<Comment> commentList = commentRepository.findAllByPost(findPost);
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

	// 내가 쓴 댓글 모아보기
	public ResponseEntity<StatusResponse> getMyCommentList(@AuthUser Member member) {
		List<Comment> myCommentList = commentRepository.findAllByWriterOrderByCreatedAtDesc(member);
		List<MyCommentResponseDto> resDtos = myCommentList.stream()
			.map(MyCommentResponseDto::of)
			.collect(Collectors.toList());
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(resDtos)
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

	// 내가 쓴 댓글의 게시글 모아보기
	public ResponseEntity<StatusResponse> getCommentedPosts(Member member) {
		List<Comment> comments = commentRepository.findByWriter(member);

		List<CommentPostDto> commentedPosts = comments.stream()
			.map(CommentPostDto::of)
			.collect(Collectors.toList());

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(commentedPosts)
			.build());
	}
}
