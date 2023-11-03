package efub.back.jupjup.domain.comment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.comment.domain.Comment;
import efub.back.jupjup.domain.comment.dto.CommentDto;
import efub.back.jupjup.domain.comment.dto.CommentRequestDto;
import efub.back.jupjup.domain.comment.exception.NoAuthorityCommentRemoveException;
import efub.back.jupjup.domain.comment.repository.CommentRepository;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import efub.back.jupjup.domain.comment.exception.NoPostExistException;
import efub.back.jupjup.domain.comment.exception.NoCommentExistsException;
import efub.back.jupjup.domain.comment.exception.NoMemberExistException;
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
			.data(CommentDto.of(comment, post, member))
			.build());
	}

	// 댓글 조회 기능
	public ResponseEntity<StatusResponse> getCommentList(Long postId) {
		Post findPost = postRepository.findById(postId).orElseThrow(NoPostExistException::new);
		List<Comment> commentList = commentRepository.findAllByPost(findPost);
		List<CommentDto> commentDtoList = new ArrayList<>();
		for (Comment comment : commentList) {
			Member member = memberRepository.findById(comment.getWriter().getId()).orElseThrow(NoMemberExistException::new);
			CommentDto commentDto = CommentDto.of(comment, findPost, member);
			commentDtoList.add(commentDto);
		}

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(commentDtoList)
			.build());
	}

	// 댓글 삭제 기능
	public ResponseEntity<StatusResponse> removeComment(Long commentId, Member member) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(NoCommentExistsException::new);

		if (!comment.getWriter().getId().equals(member.getId())) {
			throw new NoAuthorityCommentRemoveException();
		}
		comment.remove();
		commentRepository.save(comment); // 삭제 플래그만 설정, 실제 삭제는 이루어지지 않음

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.build());
	}

	// 사용자가 댓글을 작성한 게시글 리스트 반환하기
	public ResponseEntity<StatusResponse> getPostsByMemberComments(Member member) {

		List<Comment> comments = commentRepository.findByWriter(member);

		Set<Long> postIds = comments.stream()
			.map(comment -> comment.getPost().getId())
			.collect(Collectors.toSet());

		List<Post> posts = postIds.stream()
			.map(postId -> postRepository.findById(postId).orElseThrow(NoPostExistException::new))
			.collect(Collectors.toList());

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(posts)
			.build());
	}
}
