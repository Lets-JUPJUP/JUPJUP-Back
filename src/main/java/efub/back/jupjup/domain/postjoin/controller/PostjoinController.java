package efub.back.jupjup.domain.postjoin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.postjoin.dto.MemberProfileResponseDto;
import efub.back.jupjup.domain.postjoin.dto.PostjoinListResponseDto;
import efub.back.jupjup.domain.postjoin.dto.PostjoinResponseDto;
import efub.back.jupjup.domain.postjoin.service.PostjoinService;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/posts/{postId}/postjoins")
@RequiredArgsConstructor
public class PostjoinController {
	private final PostjoinService postjoinService;

	@PostMapping
	public ResponseEntity<String> postJoin(@AuthUser Member member, @PathVariable("postId") Long postId) {
		postjoinService.createByPostId(member, postId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<String> deletePostjoin(@AuthUser Member member, @PathVariable("postId") Long postId) {
		postjoinService.deleteByPostId(member, postId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<PostjoinResponseDto> checkPostJoin(@AuthUser Member member, @PathVariable("postId") Long postId) {
		PostjoinResponseDto postjoinResponseDto = postjoinService.findExistence(member, postId);
		return ResponseEntity.status(HttpStatus.OK).body(postjoinResponseDto);
	}

	@GetMapping("/joined-members")
	public ResponseEntity<List<MemberProfileResponseDto>> getJoinedMembers(@PathVariable Long postId) {
		List<MemberProfileResponseDto> members = postjoinService.findAllMembersJoinedPost(postId);
		return ResponseEntity.status(HttpStatus.OK).body(members);
	}

	@GetMapping("/joined")
	public ResponseEntity<List<PostjoinListResponseDto>> getJoinedPosts(@AuthUser Member member) {
		List<PostjoinListResponseDto> posts = postjoinService.findAllJoinedPostsByMember(member);
		return ResponseEntity.status(HttpStatus.OK).body(posts);
	}
}
