package efub.back.jupjup.domain.post.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.heart.repository.HeartRepository;
import efub.back.jupjup.domain.image.service.ImageService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostAgeRange;
import efub.back.jupjup.domain.post.domain.PostGender;
import efub.back.jupjup.domain.post.domain.PostImage;
import efub.back.jupjup.domain.post.dto.PostRequestDto;
import efub.back.jupjup.domain.post.dto.PostResponseDto;
import efub.back.jupjup.domain.post.repository.PostImageRepository;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.postjoin.domain.Postjoin;
import efub.back.jupjup.domain.postjoin.repository.PostjoinRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final PostImageRepository postImageRepository;
	private final PostjoinRepository postjoinRepository;
	private final HeartRepository heartRepository;
	private final ImageService imageService;
	private final MemberRepository memberRepository;

	private StatusResponse createStatusResponse(Object data) {
		return StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(data)
			.build();
	}

	// 플로깅 게시글 작성
	public ResponseEntity<StatusResponse> savePost(PostRequestDto requestDto, Member member) {
		Post post = requestDto.toEntity(requestDto, member);
		post.updateIsRecruitmentSuccessful(1L);
		postRepository.save(post);

		List<String> imageUrls = imageService.saveImageUrlsPost(requestDto.getImages(), post);
		boolean isJoined = true;
		boolean isEnded = false;
		boolean isHearted = false;

		PostResponseDto postResponseDto = PostResponseDto.of(post, imageUrls, Optional.of(isJoined), Optional.of(isHearted), isEnded);

		return ResponseEntity.ok(createStatusResponse(postResponseDto));
	}

	// 플로깅 게시글 상세 보기 : 1개
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPost(Long postId, Member member) {
		Post post = postRepository.findById(postId).orElseThrow();

		List<String> urlList = postImageRepository.findAllByPost(post)
			.stream()
			.map(PostImage::getFileUrl)
			.collect(Collectors.toList());

		boolean isAuthor = member.getId().equals(post.getAuthor().getId());
		boolean hasJoined = postjoinRepository.existsByMemberAndPost(member, post);
		boolean isJoined = isAuthor || hasJoined;
		boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
		boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

		PostResponseDto responseDto = PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded);
		return ResponseEntity.ok(createStatusResponse(responseDto));
	}

	// 플로깅 게시글 리스트 보기
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getAllPosts(Member member) {
		List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isAuthor = member.getId().equals(post.getAuthor().getId());
			boolean hasJoined = postjoinRepository.existsByMemberAndPost(member, post);
			boolean isJoined = isAuthor || hasJoined;
			boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 플로깅 게시글 리스트 보기 - (로그인 없이)
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getAllPostsUnAuth() {
		List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.empty(), Optional.empty(), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 성별을 기준으로 게시글 필터링 하는 기능
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByGender(String postGenderStr, Member member) {

		PostGender postGender = PostGender.valueOf(postGenderStr.toUpperCase());
		List<Post> posts = postRepository.findAllByPostGender(postGender, Sort.by(Sort.Direction.DESC, "createdAt"));

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isAuthor = member.getId().equals(post.getAuthor().getId());
			boolean hasJoined = postjoinRepository.existsByMemberAndPost(member, post);
			boolean isJoined = isAuthor || hasJoined;
			boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 성별을 기준으로 게시글 필터링 하는 기능 - (로그인 없이)
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByGenderUnAuth(String postGenderStr) {

		PostGender postGender = PostGender.valueOf(postGenderStr.toUpperCase());
		List<Post> posts = postRepository.findAllByPostGender(postGender, Sort.by(Sort.Direction.DESC, "createdAt"));

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.empty(), Optional.empty(), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 나이를 기준으로 게시글 필터링 하는 기능
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByAgeRange(PostAgeRange postAge, Member member) {

		List<Post> posts = postRepository.findAllByPostAgeRangesContaining(postAge, Sort.by(Sort.Direction.DESC, "createdAt"));

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isAuthor = member.getId().equals(post.getAuthor().getId());
			boolean hasJoined = postjoinRepository.existsByMemberAndPost(member, post);
			boolean isJoined = isAuthor || hasJoined;
			boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted),isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 나이를 기준으로 게시글 필터링 하는 기능 - (로그인 없이)
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByAgeRangeUnAuth(PostAgeRange postAge) {

		List<Post> posts = postRepository.findAllByPostAgeRangesContaining(postAge, Sort.by(Sort.Direction.DESC, "createdAt"));

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.empty(), Optional.empty(), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 반려동물 여부 기준으로 게시글 필터링 하는 기능
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByWithPet(boolean withPetValue, Member member) {

		List<Post> posts = postRepository.findAllByWithPet(withPetValue, Sort.by(Sort.Direction.DESC, "createdAt"));

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isAuthor = member.getId().equals(post.getAuthor().getId());
			boolean hasJoined = postjoinRepository.existsByMemberAndPost(member, post);
			boolean isJoined = isAuthor || hasJoined;
			boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 반려동물 여부 기준으로 게시글 필터링 하는 기능 - (로그인 없이)
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByWithPetUnAuth(boolean withPetValue) {

		List<Post> posts = postRepository.findAllByWithPet(withPetValue, Sort.by(Sort.Direction.DESC, "createdAt"));

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.empty(), Optional.empty(), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 플로깅 게시글 삭제
	public ResponseEntity<StatusResponse> deletePost(Member member, Long postId) {
		Post post = postRepository.findById(postId).orElseThrow();
		checkValidMember(member.getId(), post.getAuthor().getId());

		heartRepository.deleteByPost(post); // 게시글에 대한 모든 찜하기 삭제
		postImageRepository.deleteAllByPost(post);
		postRepository.delete(post);

		return ResponseEntity.ok(createStatusResponse("post deleted"));
	}

	private void checkValidMember(Long memberId, Long authorId) {
		if (!memberId.equals(authorId)) {
			throw new IllegalArgumentException();
		}
	}

	// 전체 플로깅 게시글 개수와 사용자가 참여한 플로깅 게시글 개수 반환
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostCounts(Member member) {
		long totalPostsCount = postRepository.count(); // 전체 플로깅 게시글의 개수
		long joinedPostsCount = postjoinRepository.countByMember(member); // 사용자가 참여한 플로깅 게시글의 개수

		Map<String, Long> counts = new HashMap<>();
		counts.put("totalPostsCount", totalPostsCount);
		counts.put("joinedPostsCount", joinedPostsCount);

		return ResponseEntity.ok(createStatusResponse(counts));
	}

	// 특정 사용자의 주최한 플로깅 개수와 참여한 플로깅 개수 조회
	public ResponseEntity<StatusResponse> getUserPostCounts(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
		long hostedPostCount = postRepository.countByAuthor(member); // 주최한 플로깅 개수
		long joinedPostCount = postjoinRepository.countByMember(member); // 참여한 플로깅 개수

		Map<String, Long> counts = new HashMap<>();
		counts.put("hostedPostCount", hostedPostCount);
		counts.put("joinedPostCount", joinedPostCount);

		return ResponseEntity.ok(createStatusResponse(counts));
	}

	// 로그인한 사용자가 주최한 플로깅 게시글을 조회하는 메소드
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getHostedPosts(Member member) {
		List<Post> hostedPosts = postRepository.findAllByAuthor(member, Sort.by(Sort.Direction.DESC, "createdAt"));
		List<PostResponseDto> hostedPostsDtos = hostedPosts.stream()
			.map(post -> {
				List<String> urlList = postImageRepository.findAllByPost(post)
					.stream()
					.map(PostImage::getFileUrl)
					.collect(Collectors.toList());

				boolean isAuthor = member.getId().equals(post.getAuthor().getId());
				boolean hasJoined = postjoinRepository.existsByMemberAndPost(member, post);
				boolean isJoined = isAuthor || hasJoined;
				boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
				boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

				return PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded);
			}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(hostedPostsDtos));
	}

	// 로그인한 사용자가 참여한 플로깅 게시글을 조회하는 메소드 (플로깅 시작 시간 이전, 이후 여부에 따라 분리)
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getJoinedPosts(Member member) {

		List<Postjoin> joinedPostjoins = postjoinRepository.findByMemberOrderByPostCreatedAtDesc(member);

		LocalDateTime now = LocalDateTime.now();
		List<PostResponseDto> activePosts = new ArrayList<>();
		List<PostResponseDto> endedPosts = new ArrayList<>();

		joinedPostjoins.forEach(postjoin -> {
			Post post = postjoin.getPost();
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
			boolean isEnded = now.isAfter(post.getStartDate());

			PostResponseDto dto = PostResponseDto.of(post, urlList, Optional.of(true),
				Optional.of(isHearted), isEnded);

			if (isEnded) {
				endedPosts.add(dto);
			} else {
				activePosts.add(dto);
			}
		});

		Map<String, Object> response = new HashMap<>();
		response.put("activePosts", activePosts);
		response.put("endedPosts", endedPosts);

		return ResponseEntity.ok(createStatusResponse(response));
	}
}
