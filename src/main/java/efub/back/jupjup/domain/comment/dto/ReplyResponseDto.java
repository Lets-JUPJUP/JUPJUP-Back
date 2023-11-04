package efub.back.jupjup.domain.comment.dto;

import java.time.LocalDateTime;

import efub.back.jupjup.domain.comment.domain.Comment;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplyResponseDto {
	private Long id;
	private String content;
	private Long parenttId;
	private Boolean isRemoved;
	private Boolean isAuthor; //글 작성자인지 여부
	private WriterInfoDto writerInfoDto;
	private LocalDateTime createdDate;

	public static ReplyResponseDto of(Comment child, Post post, Member member) {
		WriterInfoDto writerInfoDto = new WriterInfoDto(child.getWriter().getId(), child.getWriter().getNickname(), child.getWriter().getProfileImageUrl());
		return ReplyResponseDto.builder()
			.id(child.getId())
			.content(child.getContent())
			.parenttId(child.getParent().getId())
			.isRemoved(child.isRemoved())
			.isAuthor(child.getWriter().getId().equals(post.getAuthor().getId()))
			.writerInfoDto(writerInfoDto)
			.createdDate(child.getCreatedAt())
			.build();

	}
}
