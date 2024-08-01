package efub.back.jupjup.domain.score.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.score.dto.request.ScoreReqDto;
import efub.back.jupjup.domain.score.service.ScoreService;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/scores")
@RequiredArgsConstructor
public class ScoreController {
	private final ScoreService scoreService;

	@PostMapping("")
	public ResponseEntity<StatusResponse> writeReview(@AuthUser Member member, @RequestBody ScoreReqDto scoreReqDto) {
		return scoreService.giveScore(member, scoreReqDto);
	}

	@GetMapping("/{memberId}")
	public ResponseEntity<StatusResponse> getReviewScore(@PathVariable Long memberId) {
		return scoreService.getAverageScore(memberId);
	}

}
