package efub.back.jupjup.domain.post.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.AgeRange;
import efub.back.jupjup.domain.post.domain.Gender;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.dto.PostRequestDto;
import efub.back.jupjup.domain.post.dto.PostUpdateRequestDto;
import efub.back.jupjup.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;


	// 게시글 추가
	public Post addPost(Member member, PostRequestDto requestDto) throws IOException {
		String title = requestDto.getTitle();
		String content = requestDto.getContent();
		String startPlace = requestDto.getStartPlace();
		LocalDateTime startDate = requestDto.getStartDate();
		Integer minMember = requestDto.getMinMember();
		Integer maxMember = requestDto.getMaxMember();
		Gender gender = requestDto.getGender();
		AgeRange ageRange = requestDto.getAgeRange();
		LocalDateTime dueDate = requestDto.getDueDate();
		Long memberId = member.getId();

		Post post = new Post(title, content, startPlace, startDate, minMember, maxMember, gender, ageRange, dueDate, memberId);
		postRepository.save(post);

		return post;
	}

	// 게시글 수정
	public Post updatePost(Member member, Long postId, PostUpdateRequestDto requestDto) {
		Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));

		post.update(requestDto);
		return post;
	}

	// 게시글 1개씩 조회
	public Post findPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));
		return post;
	}

	// 게시글 삭제
	@Transactional
	public void deletePost(Member member, Long postId) {
		Post post = findPost(postId);
		postRepository.delete(post);
	}

	public List<Post> findAllPosts() {
		return postRepository.findAll();
	}
}
