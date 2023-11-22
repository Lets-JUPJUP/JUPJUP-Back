package efub.back.jupjup.domain.eventjoin.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.eventInfo.repository.EventInfoRepository;
import efub.back.jupjup.domain.eventjoin.domain.Eventjoin;
import efub.back.jupjup.domain.eventInfo.domain.EventInfo;
import efub.back.jupjup.domain.eventjoin.repository.EventjoinRepository;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventjoinService {
	private final EventInfoRepository eventInfoRepository;
	private final EventjoinRepository eventjoinRepository;

	// 공식행사 참여 신청
	public ResponseEntity<StatusResponse> joinEvent(Member member, Long eventInfoId) {

		EventInfo eventInfo = eventInfoRepository.findById(eventInfoId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

		Eventjoin eventjoin = new Eventjoin(member, eventInfo, true);
		eventjoinRepository.save(eventjoin);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(eventInfoId + "번 게시글에 참여하였습니다.")
			.build());
	}

	// 참여 신청 취소
	public ResponseEntity<StatusResponse> unjoinEvent(Member member, Long eventInfoId) {

		EventInfo eventInfo = eventInfoRepository.findById(eventInfoId)
			.orElseThrow(() -> new IllegalArgumentException("해당 공식행사를 찾을 수 없습니다."));

		Eventjoin existingEventjoin = eventjoinRepository.findByMemberAndEventInfo(member, eventInfo)
			.orElseThrow(() -> new RuntimeException("참여 정보가 없습니다."));

		eventjoinRepository.delete(existingEventjoin);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(eventInfoId + "번 게시글에 참여 신청을 취소하였습니다.")
			.build());
	}

	// 참여 인원 수 보기
	public ResponseEntity<StatusResponse> countEventParticipants(Long eventInfoId) {
		EventInfo eventInfo = eventInfoRepository.findById(eventInfoId)
			.orElseThrow(() -> new IllegalArgumentException("해당 공식행사를 찾을 수 없습니다."));

		Long joinCount = eventjoinRepository.countByEventInfo(eventInfo);

		Map<String, Long> responseData = new HashMap<>();
		responseData.put("joinCount", joinCount);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(responseData)
			.build());
	}
}
