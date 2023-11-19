package efub.back.jupjup.domain.postjoin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.notification.comment.domain.NotificationType;
import efub.back.jupjup.domain.notification.service.NotificationService;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.postjoin.domain.Postjoin;
import efub.back.jupjup.domain.postjoin.dto.MemberProfileResponseDto;
import efub.back.jupjup.domain.postjoin.repository.PostjoinRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostjoinService {
	private static final String RECRUITMENT_SUCCESS_MSG = "신청한 플로깅 모임이 인원을 충족하여 성사되었습니다.";
	private static final String RECRUITMENT_FAILURE_MSG = "신청한 플로깅 모임이 인원을 불충족하여 취소되었습니다.";
	private final PostRepository postRepository;
	private final PostjoinRepository postjoinRepository;
	private final NotificationService notificationService;

	// 참여 신청
	public ResponseEntity<StatusResponse> joinPost(Member member, Long postId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
		Long memberCount = postjoinRepository.countByPost(post);

		// TODO : 최대 인원을 초과할 경우 예외 발생시키는 로직 추가 (memberCount 이용)

		Postjoin postjoin = new Postjoin(member, post, true);
		postjoinRepository.save(postjoin);

		// 모집 성사 여부 업데이트
		post.updateIsRecruitmentSuccessful(memberCount + 1);
		postRepository.save(post);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(postId + "번 게시글에 참여하였습니다.")
			.build());
	}

	// 참여 신청 취소
	public ResponseEntity<StatusResponse> unjoinPost(Member member, Long postId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

		Postjoin existingPostjoin = postjoinRepository.findByMemberAndPost(member, post)
			.orElseThrow(() -> new RuntimeException("참여 정보가 없습니다."));

		postjoinRepository.delete(existingPostjoin);

		// 모집 성사 여부 업데이트
		Long memberCount = postjoinRepository.countByPost(post);
		post.updateIsRecruitmentSuccessful(memberCount);
		postRepository.save(post);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(postId + "번 게시글에 참여 신청을 취소하였습니다.")
			.build());
	}

	// 참여 신청한 멤버 조회
	public ResponseEntity<StatusResponse> getJoinedMembers(Long postId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

		List<Member> joinedMembers = postjoinRepository.findAllByPost(post)
			.stream()
			.map(Postjoin::getMember)
			.collect(Collectors.toList());

		List<MemberProfileResponseDto> memberDtos = joinedMembers.stream()
			.map(member -> new MemberProfileResponseDto(
				member.getId(),
				member.getNickname(),
				member.getProfileImageUrl(),
				member.getAgeRange(),
				member.getGender()
			))
			.collect(Collectors.toList());

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(memberDtos)
			.build());
	}

	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")  // 매 시간 정각에 실행
	public void checkRecruitmentDeadlines() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start = now.minusHours(1).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime end = now.minusHours(1).withMinute(59).withSecond(59).withNano(999999999);
		log.info(start + " ~ " + end + "사이에 모집 마감된 플로깅 성사 여부 알림 전송");

		List<Post> recentClosedFloggings = postRepository.findByDueDateBetween(start, end);
		log.info("해당하는 포스트 개수 : " + recentClosedFloggings.size());
		for (Post post : recentClosedFloggings) {
			notifyRecruitmentResult(post);
		}
	}

	private void notifyRecruitmentResult(Post post) {
		String result = RECRUITMENT_FAILURE_MSG;
		if (post.getIsRecruitmentSuccessful()) {
			result = RECRUITMENT_SUCCESS_MSG;
		}
		List<Postjoin> postjoins = postjoinRepository.findAllByPost(post);
		for (Postjoin postjoin : postjoins) {
			Member member = postjoin.getMember();
			notificationService.send(member, NotificationType.FLOGGING, result, post.getId());
		}
	}

}
