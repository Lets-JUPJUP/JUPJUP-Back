package efub.back.jupjup.domain.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.dto.ImageUploadRequestDto;
import efub.back.jupjup.domain.report.dto.ReportRequestDto;
import efub.back.jupjup.domain.report.service.ReportService;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
	private final ReportService reportService;
	@PostMapping
	public ResponseEntity<StatusResponse> submitReport(@RequestBody ReportRequestDto reportRequestDto,
		@AuthUser Member member) {
		return reportService.createReport(reportRequestDto, member);
	}

	@PostMapping("/images")
	public ResponseEntity<StatusResponse> getPresignedUrls(@AuthUser Member member,
		@RequestBody ImageUploadRequestDto imageUploadRequestDto) {
		return reportService.getPresignedUrls(member, imageUploadRequestDto);
	}
}
