package efub.back.jupjup.domain.postjoin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.service.MemberService;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.post.service.PostService;
import efub.back.jupjup.domain.postjoin.domain.Postjoin;
import efub.back.jupjup.domain.postjoin.dto.MemberProfileResponseDto;
import efub.back.jupjup.domain.postjoin.dto.PostjoinListResponseDto;
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
	private final MemberService memberService;

	// 플로깅 모집 신청하기
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

	// 플로깅 모집 신청 취소하기
	@Transactional
	public void deleteByPostId(Member member, Long postId){
		postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
		if(!postjoinRepository.existsPostjoinByMemberIdAndPostId(member.getId(), postId)){
			throw new IllegalArgumentException("플로깅 참여신청이 되지 않은 글입니다.");
		}
		Postjoin postjoin = postjoinRepository.findByMemberIdAndPostId(member.getId(), postId)
			.orElseThrow(() -> new IllegalArgumentException("플로깅 참여신청이 되지 않은 글입니다."));
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

	@Transactional(readOnly = true)
	public List<PostjoinListResponseDto> findAllJoinedPostsByMember(Member member) {
		List<Postjoin> postjoins = postjoinRepository.findByMemberId(member.getId());
		return postjoins.stream()
			.map(postjoin -> new PostjoinListResponseDto(postjoin.getPostId(),
				postRepository.findById(postjoin.getPostId()).get().getTitle())) // Assuming title exists
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<MemberProfileResponseDto> findAllMembersJoinedPost(Long postId) {
		List<Postjoin> postjoins = postjoinRepository.findByPostId(postId);
		return postjoins.stream()
			.map(postjoin -> {
				Member member = memberService.findMemberById(postjoin.getMemberId());
				return new MemberProfileResponseDto(
					member.getId(),
					member.getUsername(),
					member.getProfileImageUrl(),
					member.getNickname(),
					member.getAgeRange(),
					member.getGender()
				);
			})
			.collect(Collectors.toList());
	}
}
