package efub.back.jupjup.domain.postjoin.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.postjoin.domain.Postjoin;
import efub.back.jupjup.domain.postjoin.repository.PostjoinRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostjoinService {
	private final PostRepository postRepository;
	private final PostjoinRepository postjoinRepository;

	@Transactional
	public ResponseEntity<StatusResponse> joinPost(Member member, Long postId){

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

	@Transactional
	public ResponseEntity<StatusResponse> unjoinPost(Member member, Long postId){

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

}
