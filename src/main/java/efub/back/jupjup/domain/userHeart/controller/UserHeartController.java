package efub.back.jupjup.domain.userHeart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.domain.userHeart.dto.request.UserHeartReqDto;
import efub.back.jupjup.domain.userHeart.service.UserHeartService;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/user-hearts")
@RequiredArgsConstructor
public class UserHeartController {
	private final UserHeartService userHeartService;

	@PostMapping()
	public ResponseEntity<StatusResponse> postUserHearts(@AuthUser Member member, @RequestBody UserHeartReqDto reqDto) {
		return userHeartService.giveUserHeart(member, reqDto);
	}

	@GetMapping("/{memberId}")
	public ResponseEntity<StatusResponse> getUserHearts(@PathVariable Long memberId) {
		return userHeartService.getUserHearts(memberId);
	}
}
