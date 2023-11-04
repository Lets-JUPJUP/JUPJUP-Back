package efub.back.jupjup.domain.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

import efub.back.jupjup.domain.comment.domain.Comment;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDto {
	private Long id;
	private String content;
	private Boolean isRemoved;
	private Boolean isAuthor; //글 작성자인지 여부
	private WriterInfoDto writerInfoDto;
	private LocalDateTime createdDate;
	private List<ReplyResponseDto> replyList;
	private Long parentId;

	public static CommentDto of(Comment comment, Post post, Member member, List<ReplyResponseDto> replyList) {
		WriterInfoDto writerInfoDto = new WriterInfoDto(comment.getWriter().getId(), member.getNickname(), member.getProfileImageUrl());
		CommentDtoBuilder builder = CommentDto.builder()
			.id(comment.getId())
			.content(comment.getContent())
			.isRemoved(comment.isRemoved())
			.writerInfoDto(writerInfoDto)
			.isAuthor(comment.getWriter().getId().equals(post.getAuthor().getId()))
			.createdDate(comment.getCreatedAt());

		if (comment.getParent() != null) {
			builder.parentId(comment.getParent().getId());
		}
		if (replyList != null) {
			builder.replyList(replyList);
		}
		CommentDto commentDto = builder.build();
		return commentDto;
	}
}
