package efub.back.jupjup.domain.report.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.image.service.ImageService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.report.domain.Report;
import efub.back.jupjup.domain.report.dto.ReportRequestDto;
import efub.back.jupjup.domain.report.dto.ReportResponseDto;
import efub.back.jupjup.domain.report.repository.ReportRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReportService {
	private final ReportRepository reportRepository;
	private final ImageService imageService;

	private StatusResponse createStatusResponse(Object data) {
		return StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(data)
			.build();
	}

	// 신고글 작성하기
	public ResponseEntity<StatusResponse> createReport(ReportRequestDto reportRequestDto, Member member) {
		Report report = reportRequestDto.toEntity(reportRequestDto, member);
		reportRepository.save(report);

		List<String> imageUrls = new ArrayList<>();
		if (reportRequestDto.getImages() != null && !reportRequestDto.getImages().isEmpty()) {
			imageUrls = imageService.saveImageUrlsReport(reportRequestDto.getImages(), report);
		}
		ReportResponseDto reportResponseDto = ReportResponseDto.of(report, imageUrls);

		return ResponseEntity.ok(createStatusResponse(reportResponseDto));
	}
}
