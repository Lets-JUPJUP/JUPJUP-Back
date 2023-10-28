package efub.back.jupjup.domain.postjoin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.postjoin.domain.Postjoin;
import efub.back.jupjup.domain.postjoin.dto.PostjoinResponseDto;
import efub.back.jupjup.domain.postjoin.repository.PostjoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostjoinService {
	private final PostjoinRepository postjoinRepository;
	private final PostRepository postRepository;

	@Transactional
	public void createByPostId(Member member, Long postId){
		postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
		if(postjoinRepository.existsPostjoinByMemberIdAndPostId(member.getId(), postId)){
			throw new IllegalArgumentException("이미 참여신청한 플로깅입니다");
		}
		Postjoin postjoin = new Postjoin(member.getId(), postId);
		postjoinRepository.save(postjoin);
	}

	@Transactional
	public void deleteByPostId(Member member, Long postId){
		postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
		if(!postjoinRepository.existsPostjoinByMemberIdAndPostId(member.getId(), postId)){
			throw new IllegalArgumentException("플로깅 참여신청 취소");
		}
		Postjoin postjoin = postjoinRepository.findByMemberIdAndPostId(member.getId(), postId)
			.orElseThrow(() -> new IllegalArgumentException("플로깅 참여신청 취소"));
		postjoinRepository.delete(postjoin);
	}

	@Transactional(readOnly = true)
	public PostjoinResponseDto findExistence(Member member, Long postId){
		postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
		if(postjoinRepository.existsPostjoinByMemberIdAndPostId(member.getId(), postId)){
			return new PostjoinResponseDto(true);
		}
		else{
			return new PostjoinResponseDto(false);
		}
	}
}
