package efub.back.jupjup.domain.admin.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.admin.report.service.AdminReportService;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins/reports")
public class AdminReportController {
	private final AdminReportService adminReportService;

	// 모든 신고 내역 조회하기
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping
	public ResponseEntity<StatusResponse> getAllReports() {
		return adminReportService.getAllReports();
	}
}
