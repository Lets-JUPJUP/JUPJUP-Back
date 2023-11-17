package efub.back.jupjup.domain.eventcomment.dto;

import java.time.LocalDateTime;

import efub.back.jupjup.domain.comment.dto.WriterInfoDto;
import efub.back.jupjup.domain.eventInfo.domain.EventInfo;
import efub.back.jupjup.domain.eventcomment.domain.Eventcomment;
import efub.back.jupjup.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventcommentDto {
	private Long id;
	private String content;
	private WriterInfoDto writerInfoDto;
	private LocalDateTime createdDate;

	public static EventcommentDto of(Eventcomment eventcomment, EventInfo eventInfo, Member member){
		WriterInfoDto writerInfoDto1 = new WriterInfoDto(eventcomment.getWriter().getId(), member.getNickname(),
			member.getProfileImageUrl());
		EventcommentDtoBuilder builder = EventcommentDto.builder()
			.id(eventcomment.getId())
			.content(eventcomment.getContent())
			.writerInfoDto(writerInfoDto1)
			.createdDate(eventcomment.getCreatedAt());
		EventcommentDto eventcommentDto = builder.build();
		return eventcommentDto;
	}
}
