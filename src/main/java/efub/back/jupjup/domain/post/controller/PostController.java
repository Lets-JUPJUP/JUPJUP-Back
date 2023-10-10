package efub.back.jupjup.domain.post.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.dto.PostRequestDto;
import efub.back.jupjup.domain.post.dto.PostResponseDto;
import efub.back.jupjup.domain.post.dto.PostUpdateRequestDto;
import efub.back.jupjup.domain.post.service.PostService;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;

	// 게시글 생성
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public PostResponseDto createPost(
		@AuthUser Member member,
		@RequestPart(value="dto") PostRequestDto requestDto) throws IOException {

		Post post = postService.addPost(member,requestDto);
		return new PostResponseDto(post,member);
	}

	// 게시글 수정
	@PutMapping("/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public PostResponseDto updatePost(@PathVariable Long postId,
		@RequestPart(value="dto") PostUpdateRequestDto requestDto, @AuthUser Member member) {
		Post post = postService.updatePost(member, postId, requestDto);
		return new PostResponseDto(post, member);
	}

	// 게시글 조회 (1개)
	@GetMapping("/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public PostResponseDto getPost(@PathVariable Long postId, @AuthUser Member member) {
		Post post = postService.findPost(postId);
		return new PostResponseDto(post, member);
	}

	// 게시글 전체 리스트 조회
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<PostResponseDto> getAllPosts(@AuthUser Member member) {
		List<Post> posts = postService.findAllPosts();
		return posts.stream().map(post -> new PostResponseDto(post, member)).collect(Collectors.toList());
	}


	// 게시글 삭제
	@DeleteMapping("/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public String deletePost(@AuthUser Member member, @PathVariable Long postId) throws IOException {
		Post post = postService.findPost(postId);

		postService.deletePost(member, postId);
		return "성공적으로 삭제되었습니다.";
	}
}
