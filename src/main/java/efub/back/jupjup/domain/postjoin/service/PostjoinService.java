package efub.back.jupjup.domain.postjoin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessagingException;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.notification.domain.NotificationType;
import efub.back.jupjup.domain.notification.service.FirebaseService;
import efub.back.jupjup.domain.notification.service.NotificationService;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostGender;
import efub.back.jupjup.domain.post.exception.MaxMemberLimitException;
import efub.back.jupjup.domain.post.exception.MismatchPostGenderException;
import efub.back.jupjup.domain.post.exception.PostNotFoundException;
import efub.back.jupjup.domain.post.exception.PostjoinNotFoundException;
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
	private static final String RECRUITMENT_SUCCESS_MSG_MEMBER = "신청한 플로깅 모임이 인원을 충족하여 성사되었습니다.";
	private static final String RECRUITMENT_SUCCESS_MSG_HOST = "주최한 플로깅 모임이 인원을 충족하여 성사되었습니다.";
	private static final String RECRUITMENT_FAILURE_MSG_MEMBER = "신청한 플로깅 모임이 인원을 불충족하여 취소되었습니다.";
	private static final String RECRUITMENT_FAILURE_MSG_HOST = "주최한 플로깅 모임이 인원을 불충족하여 취소되었습니다.";

	private final PostRepository postRepository;
	private final PostjoinRepository postjoinRepository;
	private final NotificationService notificationService;
	private final FirebaseService firebaseService;

	// 참여 신청
	public ResponseEntity<StatusResponse> joinPost(Member member, Long postId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(postId));
		Long memberCount = postjoinRepository.countByPost(post);

		// 최대 인원을 초과할 경우 예외 발생
		Long maxMember = new Long(post.getMaxMember());
		if (memberCount >= maxMember - 1) {
			throw new MaxMemberLimitException();
		}

		// 성별 제한이 있는 경우, 성별이 맞는지 확인
		if (!post.getPostGender().equals(PostGender.ANY) && !member.getGender()
			.name()
			.equalsIgnoreCase(post.getPostGender().name())) {
			throw new MismatchPostGenderException();
		}

		// TODO : ageRange ->  age를 이용한 로직으로 수정 필요
		// 나이 제한이 있는 경우, 나이 범위가 맞는지 확인
		// if (!post.getPostAgeRanges().contains(PostAgeRange.AGE_ANY) &&
		// 	post.getPostAgeRanges().stream().noneMatch(ageRange -> ageRange.includes(member.getAgeRange()))) {
		// 	throw new MismatchPostAgeRangeException();
		// }

		Postjoin postjoin = new Postjoin(member, post, true);
		postjoinRepository.save(postjoin);

		// 모집 성사 여부 업데이트
		log.info("memberCount : " + memberCount);
		post.updateIsRecruitmentSuccessful(memberCount + 2);
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
			.orElseThrow(() -> new PostNotFoundException(postId));

		Postjoin existingPostjoin = postjoinRepository.findByMemberAndPost(member, post)
			.orElseThrow(() -> new PostjoinNotFoundException());

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
			.orElseThrow(() -> new PostNotFoundException(postId));

		List<Member> joinedMembers = postjoinRepository.findAllByPost(post)
			.stream()
			.map(Postjoin::getMember)
			.collect(Collectors.toList());

		List<MemberProfileResponseDto> memberDtos = joinedMembers.stream()
			.map(member -> new MemberProfileResponseDto(
				member.getId(),
				member.getNickname(),
				member.getProfileImageUrl(),
				member.getAge(),
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
	public void checkRecruitmentDeadlines() throws FirebaseMessagingException {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start;
		LocalDateTime end;

		int hour = now.getHour();

		if (hour == 1) {
			start = now.withHour(0);
		} else if (hour == 0) {
			start = now.withHour(23);
		} else {
			start = now.minusHours(1);
		}

		start = start.withMinute(0).withSecond(0).withNano(0);
		end = start.withMinute(59).withSecond(59).withNano(999999999);

		log.info(start + " ~ " + end + "사이에 모집 마감된 플로깅 성사 여부 알림 전송");

		List<Post> recentClosedPloggings = postRepository.findByDueDateBetween(start, end);
		log.info("해당하는 포스트 개수 : " + recentClosedPloggings.size());
		for (Post post : recentClosedPloggings) {
			notifyRecruitmentResult(post);
		}
	}

	private void notifyRecruitmentResult(Post post) throws FirebaseMessagingException {
		Boolean isRecruitmentSuccessful = post.getIsRecruitmentSuccessful();
		if (isRecruitmentSuccessful) {
			notifyRecruitmentSuccess(post);
		} else {
			notifyRecruitmentFailure(post);
		}
	}

	private void notifyRecruitmentSuccess(Post post) throws FirebaseMessagingException {
		String result = null;
		List<Postjoin> postjoins = postjoinRepository.findAllByPost(post);

		// 주최자 알림 전송
		Member host = post.getAuthor();
		result = RECRUITMENT_SUCCESS_MSG_HOST;
		notificationService.send(host, NotificationType.PLOGGING, result, post.getId());
		firebaseService.sendPushMessage(host.getId(), "플로깅 알림", "주최한 플로깅이 모집에 성공했습니다.");

		// 멤버 알림 전송
		for (Postjoin postjoin : postjoins) {
			Member member = postjoin.getMember();
			result = RECRUITMENT_SUCCESS_MSG_MEMBER;
			notificationService.send(member, NotificationType.PLOGGING, result, post.getId());
			firebaseService.sendPushMessage(member.getId(), "플로깅 알림", "참여한 플로깅이 모집에 성공했습니다.");
		}

	}

	private void notifyRecruitmentFailure(Post post) throws FirebaseMessagingException {
		String result = null;
		List<Postjoin> postjoins = postjoinRepository.findAllByPost(post);

		// 주최자 알림 전송
		Member host = post.getAuthor();
		result = RECRUITMENT_FAILURE_MSG_HOST;
		notificationService.send(host, NotificationType.PLOGGING, result, post.getId());
		firebaseService.sendPushMessage(host.getId(), "플로깅 알림", "주최한 플로깅이 모집에 실패했습니다.");

		// 멤버 알림 전송
		for (Postjoin postjoin : postjoins) {
			Member member = postjoin.getMember();
			result = RECRUITMENT_FAILURE_MSG_MEMBER;
			notificationService.send(member, NotificationType.PLOGGING, result, post.getId());
			firebaseService.sendPushMessage(member.getId(), "플로깅 알림", "참여한 플로깅이 모집에 실패했습니다.");
		}
	}

}
