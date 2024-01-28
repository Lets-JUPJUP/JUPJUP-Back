package efub.back.jupjup.domain.admin.event.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.admin.event.dto.AdminEventRequestDto;
import efub.back.jupjup.domain.admin.event.dto.AdminEventResponseDto;
import efub.back.jupjup.domain.eventInfo.domain.EventInfo;
import efub.back.jupjup.domain.eventInfo.repository.EventInfoRepository;
import efub.back.jupjup.domain.eventcomment.exception.NoEventInfoException;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminEventService {
	private final EventInfoRepository eventInfoRepository;

	private StatusResponse createStatusResponse(Object data) {
		return StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(data)
			.build();
	}

	// 공식 행사 게시글 작성하기
	public ResponseEntity<StatusResponse> createEvent(AdminEventRequestDto requestDto) {
		EventInfo eventInfo = new EventInfo(
			requestDto.getTitle(),
			requestDto.getInfoUrl(),
			requestDto.getImageUrl()
		);
		EventInfo savedEventInfo = eventInfoRepository.save(eventInfo);
		AdminEventResponseDto responseDto = AdminEventResponseDto.of(savedEventInfo);
		return ResponseEntity.ok(createStatusResponse(responseDto));
	}

	// 공식 행사 리스트 보기
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getAllEventInfos() {
		List<EventInfo> eventInfos = eventInfoRepository.findAll();
		List<AdminEventResponseDto> responseDtos = eventInfos.stream()
			.map(eventInfo -> AdminEventResponseDto.of(eventInfo))
			.collect(Collectors.toList());
		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 공식행사 게시글 삭제
	public ResponseEntity<StatusResponse> deleteEvent(Long eventId) {
		EventInfo eventInfo = eventInfoRepository.findById(eventId)
			.orElseThrow(() -> new NoEventInfoException());
		eventInfoRepository.delete(eventInfo);
		return ResponseEntity.ok(createStatusResponse("EventInfo deleted success."));
	}
}
