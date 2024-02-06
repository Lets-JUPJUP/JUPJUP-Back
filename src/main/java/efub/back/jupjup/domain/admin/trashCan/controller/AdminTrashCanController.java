package efub.back.jupjup.domain.admin.trashCan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.admin.trashCan.service.AdminTrashCanService;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admins/trashCans")
@RequiredArgsConstructor
public class AdminTrashCanController {

	private final AdminTrashCanService adminTrashCanService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/list")
	public ResponseEntity<StatusResponse> getTrashCans(@RequestParam(value = "pageNo") Integer pageNo) {
		return adminTrashCanService.getTrashCans(pageNo);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/{trashCanId}")
	public ResponseEntity<StatusResponse> getTrashCan(@PathVariable Long trashCanId) {
		return adminTrashCanService.getTrashCan(trashCanId);
	}
}
