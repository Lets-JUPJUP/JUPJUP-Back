package efub.back.jupjup.domain.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.admin.service.AdminMemberService;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admins/member")
@RequiredArgsConstructor
public class AdminMemberController {

	private final AdminMemberService adminMemberService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{memberId}")
	public ResponseEntity<StatusResponse> banMember(@PathVariable Long memberId) {
		return adminMemberService.blockMember(memberId);
	}
}
