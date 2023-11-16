package efub.back.jupjup.domain.eventcomment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import efub.back.jupjup.domain.eventInfo.domain.EventInfo;
import efub.back.jupjup.domain.eventInfo.repository.EventInfoRepository;
import efub.back.jupjup.domain.eventcomment.domain.Eventcomment;
import efub.back.jupjup.domain.eventcomment.dto.EventcommentDto;
import efub.back.jupjup.domain.eventcomment.dto.EventcommentRequestDto;
import efub.back.jupjup.domain.eventcomment.dto.EventcommentResponseDto;
import efub.back.jupjup.domain.eventcomment.repository.EventcommentRepository;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import efub.back.jupjup.domain.eventcomment.exception.NoEventInfoException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EventcommentService {
	private final EventcommentRepository eventcommentRepository;
	private final EventInfoRepository eventInfoRepository;

	private StatusResponse createStatusResponse(Object data) {
		return StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(data)
			.build();
	}

	// 공식행사 댓글 생성 기능
	public ResponseEntity<StatusResponse> addEventcomment(Long eventInfoId, EventcommentRequestDto requestDto, Member writer) {
		EventInfo eventInfo = eventInfoRepository.findById(eventInfoId).orElseThrow(NoEventInfoException::new);
		Eventcomment eventcomment = eventcommentRepository.save(Eventcomment.builder()
			.content(requestDto.getContent())
			.writer(writer)
			.eventInfo(eventInfo)
			.build());

		EventcommentDto eventcommentDto = EventcommentDto.of(eventcomment, eventInfo, writer);
		return ResponseEntity.ok(createStatusResponse(eventcommentDto));
	}

	// 공식행사 댓글 조회 기능
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getAllEventcomments(Long eventInfoId) {
		EventInfo eventInfo = eventInfoRepository.findById(eventInfoId).orElseThrow(NoEventInfoException::new);

		List<Eventcomment> eventcomments = eventcommentRepository.findAllByEventInfoId(eventInfoId);
		List<EventcommentDto> eventcommentDtos = eventcomments.stream()
			.map(eventcomment -> EventcommentDto.of(eventcomment, eventInfo, eventcomment.getWriter()))
			.collect(Collectors.toList());
		EventcommentResponseDto responseDto = new EventcommentResponseDto(eventcommentDtos, eventcommentDtos.size());
		return ResponseEntity.ok(createStatusResponse(responseDto));
	}

	// 공식행사 댓글 삭제 기능
	public ResponseEntity<StatusResponse> deleteEventcomment(Long eventInfoId, Long eventCommentId, Member writer) {
		Eventcomment eventcomment = eventcommentRepository.findByIdAndEventInfoIdAndWriterId(eventCommentId, eventInfoId, writer.getId())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 삭제할 권한이 없습니다."));

		eventcommentRepository.delete(eventcomment);
		return ResponseEntity.ok(createStatusResponse("댓글이 성공적으로 삭제되었습니다."));
	}
}
