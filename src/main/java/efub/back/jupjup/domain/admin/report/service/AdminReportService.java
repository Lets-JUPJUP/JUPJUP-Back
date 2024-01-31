package efub.back.jupjup.domain.admin.report.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.admin.report.dto.AdminReportResponseDto;
import efub.back.jupjup.domain.report.domain.Report;
import efub.back.jupjup.domain.report.domain.ReportImage;
import efub.back.jupjup.domain.report.repository.ReportImageRepository;
import efub.back.jupjup.domain.report.repository.ReportRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminReportService {
	private final ReportRepository reportRepository;
	private final ReportImageRepository reportImageRepository;

	private StatusResponse createStatusResponse(Object data) {
		return StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(data)
			.build();
	}

	// 모든 신고 내역 조회하기
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getAllReports() {
		List<Report> reports = reportRepository.findAll();
		int reportCount = reports.size();

		List<AdminReportResponseDto> responseDtos = reports.stream()
			.map(report -> AdminReportResponseDto.of(report, reportCount))
			.collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}
}
