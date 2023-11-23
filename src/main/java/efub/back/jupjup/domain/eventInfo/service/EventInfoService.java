package efub.back.jupjup.domain.eventInfo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.eventInfo.domain.EventInfo;
import efub.back.jupjup.domain.eventInfo.dto.EventInfoResponseDto;
import efub.back.jupjup.domain.eventInfo.repository.EventInfoRepository;
import efub.back.jupjup.domain.eventjoin.repository.EventjoinRepository;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventInfoService {
	private final EventInfoRepository eventInfoRepository;
	private final EventjoinRepository eventjoinRepository;

	private StatusResponse createStatusResponse(Object data) {
		return StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(data)
			.build();
	}
	// 공식 행사 상세 보기 : 1개
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getEventInfo(Long eventInfoId, Member member){
		EventInfo eventInfo = eventInfoRepository.findById(eventInfoId).orElseThrow();

		boolean isJoined = eventjoinRepository.findByMemberAndEventInfo(member, eventInfo).isPresent();
		EventInfoResponseDto responseDto = EventInfoResponseDto.of(eventInfo, isJoined);
		return ResponseEntity.ok(createStatusResponse(responseDto));
	}


	// 공식 행사 리스트 보기
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getAllEventInfos() {
		List<EventInfo> eventInfos = eventInfoRepository.findAll();
		List<EventInfoResponseDto> responseDtos = eventInfos.stream()
			.map(eventInfo -> EventInfoResponseDto.of(eventInfo))
			.collect(Collectors.toList());
		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

}
