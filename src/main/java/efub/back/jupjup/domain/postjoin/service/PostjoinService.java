package efub.back.jupjup.domain.postjoin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.member.domain.Member;
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
	private final PostRepository postRepository;
	private final PostjoinRepository postjoinRepository;

	// 참여 신청
	public ResponseEntity<StatusResponse> joinPost(Member member, Long postId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

		Postjoin postjoin = new Postjoin(member, post, true);
		postjoinRepository.save(postjoin);

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
				member.getUsername(),
				member.getProfileImageUrl(),
				member.getNickname(),
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
}
