package efub.back.jupjup.domain.post.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.service.MemberService;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.dto.ImageUploadRequestDto;
import efub.back.jupjup.domain.post.dto.ImageUploadResponseDto;
import efub.back.jupjup.domain.post.dto.PostRequestDto;
import efub.back.jupjup.domain.post.dto.PostResponseDto;
import efub.back.jupjup.domain.post.service.PostService;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;
	private final MemberService memberService;

	// 게시글 작성
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public PostResponseDto createPost(
		@AuthUser Member member,
		@RequestBody PostRequestDto requestDto) throws IOException {

		Post post = postService.addPost(member, requestDto);

		return new PostResponseDto(post, member);
	}

	// 전체 게시글 리스트 조회
	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public List<PostResponseDto> getPostList(){
		List<Post> posts = postService.findPostList();
		List<PostResponseDto> responseDtoList = new ArrayList<>();

		for(Post post:posts){
			Member member = memberService.findMemberById(post.getMemberId());

			responseDtoList.add(new PostResponseDto(post,member));
		}

		return responseDtoList;
	}

	// 전체 게시글 조회 : 1개
	@GetMapping("/{postId}")
	@ResponseStatus(value = HttpStatus.OK)
	public PostResponseDto getPost(@PathVariable Long postId){
		Post post = postService.findPost(postId);
		Member member = memberService.findMemberById(post.getMemberId());

		PostResponseDto responseDto = new PostResponseDto(post, member);
		return responseDto;
	}

	// 게시글 삭제
	@DeleteMapping("/{postId}")
	@ResponseStatus(HttpStatus.OK)
	public String deletePost(@AuthUser Member member, @PathVariable Long postId) throws IOException {
		Post post = postService.findPost(postId);
		postService.deletePost(member,postId);

		return "성공적으로 삭제되었습니다!";
	}

	@PostMapping("/images")
	@ResponseStatus(value = HttpStatus.OK)
	public List<ImageUploadResponseDto> getPresignedUrls(@AuthUser Member member,
		@RequestBody ImageUploadRequestDto imageUploadRequestDto) {
		return postService.getPresignedUrls(member, imageUploadRequestDto);
	}
}
