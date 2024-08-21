package efub.back.jupjup.domain.userHeart.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.exception.MemberNotFoundException;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.exception.PostNotFoundException;
import efub.back.jupjup.domain.post.exception.PostjoinNotFoundException;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.postjoin.domain.Postjoin;
import efub.back.jupjup.domain.postjoin.repository.PostjoinRepository;
import efub.back.jupjup.domain.userHeart.domain.UserHeart;
import efub.back.jupjup.domain.userHeart.dto.request.UserHeartReqDto;
import efub.back.jupjup.domain.userHeart.repository.UserHeartRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserHeartService {
	private final UserHeartRepository userHeartRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final PostjoinRepository postjoinRepository;

	public ResponseEntity<StatusResponse> giveUserHeart(Member writer, UserHeartReqDto userHeartReqDto) {
		Post post = postRepository.findById(userHeartReqDto.getPostId())
			.orElseThrow(PostNotFoundException::new);

		userHeartReqDto.getTargetIds().forEach(id -> {
			Member target = memberRepository.findById(id)
				.orElseThrow(MemberNotFoundException::new);

			checkPloggingMember(writer, target, post);

			UserHeart userHeart = new UserHeart(target, post);
			userHeartRepository.save(userHeart);
		});

		return ResponseEntity.ok()
			.body(StatusResponse.builder()
				.status(StatusEnum.OK.getStatusCode())
				.message(StatusEnum.OK.getCode())
				.data("좋아요 처리 성공")
				.build());
	}

	public ResponseEntity<StatusResponse> getUserHearts(Long memberId) {
		Long result = userHeartRepository.countUserHeartsByMemberId(memberId);

		return ResponseEntity.ok()
			.body(StatusResponse.builder()
				.status(StatusEnum.OK.getStatusCode())
				.message(StatusEnum.OK.getCode())
				.data("좋아요 수: " + result.intValue())
				.build());

	}

	// 플로깅에 참여한 유저들인지 확인
	private void checkPloggingMember(Member writer, Member target, Post post) {
		List<Member> joinedMembers = postjoinRepository.findAllByPost(post)
			.stream()
			.map(Postjoin::getMember)
			.collect(Collectors.toList());
		Member author = post.getAuthor();
		joinedMembers.add(author);

		Optional<Member> memberOptional = joinedMembers.stream()
			.filter(m -> m.getId().equals(writer.getId()))
			.findFirst();
		Optional<Member> targetOptional = joinedMembers.stream()
			.filter(m -> m.getId().equals(target.getId()))
			.findFirst();

		if (memberOptional.isEmpty() && targetOptional.isEmpty()) {
			throw new PostjoinNotFoundException(writer.getId(), target.getId());
		} else if (memberOptional.isEmpty()) {
			throw new PostjoinNotFoundException(writer.getId());
		} else if (targetOptional.isEmpty()) {
			throw new PostjoinNotFoundException(target.getId());
		}
	}
}
