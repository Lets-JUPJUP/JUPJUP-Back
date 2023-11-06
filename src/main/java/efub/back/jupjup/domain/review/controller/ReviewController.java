package efub.back.jupjup.domain.review.controller;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.review.dto.request.ReviewReqDto;
import efub.back.jupjup.domain.review.service.ReviewService;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("")
    public ResponseEntity<StatusResponse> writeReview(@AuthUser Member member, @RequestBody ReviewReqDto reviewReqDto){
        return reviewService.writeReview(member, reviewReqDto);
    }

    @GetMapping("/top3/{memberId}")
    public ResponseEntity<StatusResponse> getTop3Reviews(@PathVariable Long memberId){
        return reviewService.findTop3Badges(memberId);
    }
}
