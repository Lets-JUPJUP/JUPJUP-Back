package efub.back.jupjup.domain.admin.trashCan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import efub.back.jupjup.domain.admin.trashCan.dto.BinFeedbackStatisticDto;
import efub.back.jupjup.domain.admin.trashCan.dto.TrashCanDetailDto;
import efub.back.jupjup.domain.admin.trashCan.dto.TrashCanDto;
import efub.back.jupjup.domain.admin.trashCan.dto.TrashCanListDto;
import efub.back.jupjup.domain.admin.trashCan.dto.TrashCanSimpleDto;
import efub.back.jupjup.domain.trashCan.domain.Feedback;
import efub.back.jupjup.domain.trashCan.domain.TrashCan;
import efub.back.jupjup.domain.trashCan.exception.TrashCanNotFoundException;
import efub.back.jupjup.domain.trashCan.repository.BinFeedbackRepository;
import efub.back.jupjup.domain.trashCan.repository.TrashCanRepository;
import efub.back.jupjup.global.response.PageInfo;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminTrashCanService {

	private final TrashCanRepository trashCanRepository;
	private final BinFeedbackRepository binFeedbackRepository;

	public ResponseEntity<StatusResponse> getTrashCans(Integer pageNo) {
		int pageSize = 15;
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<TrashCan> trashCanPage = trashCanRepository.findAll(pageable);

		List<TrashCan> trashCans = trashCanPage.getContent();

		// 쓰레기통별 피드백 개수 조회
		List<Object[]> feedbackCountsByTrashCan = binFeedbackRepository.countFeedbacksByTrashCan();
		Map<Long, Long> feedbackCountMap = feedbackCountsByTrashCan.stream()
			.collect(Collectors.toMap(
				array -> (Long)array[0],   // trashCanId
				array -> (Long)array[1]    // feedbackCount
			));
		List<TrashCanSimpleDto> trashCanSimpleDtos = trashCans.stream()
			.map(e -> TrashCanSimpleDto.from(e, feedbackCountMap.getOrDefault(e.getId(), 0L)))
			.collect(Collectors.toList());

		TrashCanListDto resDto = TrashCanListDto.builder()
			.pageInfo(createPageInfo(trashCanPage))
			.trashCans(trashCanSimpleDtos)
			.build();

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(resDto)
			.build());
	}

	public ResponseEntity<StatusResponse> getTrashCan(Long trashCanId) {

		TrashCan trashCan = trashCanRepository.findById(trashCanId).orElseThrow(TrashCanNotFoundException::new);
		TrashCanDto trashCanDto = TrashCanDto.from(trashCan);
		Long feedbackCount = binFeedbackRepository.countByTrashCanId(trashCanId);
		List<BinFeedbackStatisticDto> feedbacks = new ArrayList<>();
		for (int i = 0; i < Feedback.values().length; i++) {
			Long count = binFeedbackRepository.countByFeedbackAndTrashCanId(Feedback.getFeedbackByCode(i), trashCanId);
			BinFeedbackStatisticDto statistic = BinFeedbackStatisticDto.from(Feedback.getFeedbackByCode(i), count);
			feedbacks.add(statistic);
		}
		TrashCanDetailDto resDto = TrashCanDetailDto.builder()
			.trashCanDto(trashCanDto)
			.feedbackCount(feedbackCount)
			.feedbacks(feedbacks)
			.build();

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(resDto)
			.build());
	}

	private PageInfo createPageInfo(Page<?> page) {
		return new PageInfo(page.getNumber(), page.getNumberOfElements(), (int)page.getTotalElements(),
			page.getTotalPages());
	}
}
