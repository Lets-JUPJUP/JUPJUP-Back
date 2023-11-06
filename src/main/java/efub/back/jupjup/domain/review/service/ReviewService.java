package efub.back.jupjup.domain.review.service;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.exception.MemberNotFoundException;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.review.domain.Badge;
import efub.back.jupjup.domain.review.domain.Review;
import efub.back.jupjup.domain.review.dto.request.ReviewReqDto;
import efub.back.jupjup.domain.review.dto.response.BadgeInfo;
import efub.back.jupjup.domain.review.dto.response.Top3BadgeResDto;
import efub.back.jupjup.domain.review.exception.BadgeNotExistsForCodeException;
import efub.back.jupjup.domain.review.repository.ReviewRepository;
import efub.back.jupjup.global.redis.RedisReviewCountService;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final RedisReviewCountService reviewCountService;
    private final MemberRepository memberRepository;

    public ResponseEntity<StatusResponse> writeReview(Member member, ReviewReqDto reviewReqDto){
        List<Integer> badgeList = reviewReqDto.getBadgeList();
        List<Review> savedReviewList = null;
        for(Integer code : badgeList){
            String badgeCode = String.valueOf(code);
            boolean isCodeExists = Badge.isCodeExists(badgeCode);
            if (isCodeExists) {
                Badge badge = Badge.getBadgeByCode(badgeCode);
                log.info(badge.getTitle());
                reviewCountService.incrementReviewCountForMemberByBadge(member.getId(), badge);
            } else {
                throw new BadgeNotExistsForCodeException();
            }
        }
        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(savedReviewList)
                .build());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponse> findTop3Badges(Long memberId) {
        if(!memberRepository.existsById(memberId)){
            throw new MemberNotFoundException();
        }
        Set<TypedTuple<String>> top3Badges = reviewCountService.getTop3BadgesForMember(memberId);

        List<BadgeInfo> badges = new ArrayList<>();
        int numberOfBadgesDistinct = top3Badges.size();
        if(numberOfBadgesDistinct != 0){
            for(TypedTuple<String> badgeTuple : top3Badges){
                Badge badge = Badge.getBadgeByCode(badgeTuple.getValue().toString());
                Integer count = badgeTuple.getScore().intValue();
                BadgeInfo badgeInfo = BadgeInfo.builder()
                        .code(badge.getCode())
                        .title(badge.getTitle())
                        .count(count)
                        .build();
                badges.add(badgeInfo);
            }
        }
        Top3BadgeResDto resDto = Top3BadgeResDto.builder()
                .badges(badges)
                .badgeCount(numberOfBadgesDistinct)
                .build();

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(resDto)
                .build());
    }
}
