package efub.back.jupjup.domain.post.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.heart.repository.HeartRepository;
import efub.back.jupjup.domain.image.service.ImageService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.member.exception.MemberNotFoundException;
import efub.back.jupjup.domain.member.repository.MemberRepository;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostGender;
import efub.back.jupjup.domain.post.domain.PostImage;
import efub.back.jupjup.domain.post.dto.PostFilterDto;
import efub.back.jupjup.domain.post.dto.PostRequestDto;
import efub.back.jupjup.domain.post.dto.PostResponseDto;
import efub.back.jupjup.domain.post.exception.PostNotFoundException;
import efub.back.jupjup.domain.post.repository.PostImageRepository;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.post.specification.PostSpecification;
import efub.back.jupjup.domain.postjoin.domain.Postjoin;
import efub.back.jupjup.domain.postjoin.repository.PostjoinRepository;
import efub.back.jupjup.domain.score.repository.ScoreRepository;
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
	private final ScoreRepository scoreRepository;

	private StatusResponse createStatusResponse(Object data) {
		return StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(data)
			.build();
	}

	// PostResponseDto 리스트 생성 메소드
	private List<PostResponseDto> createPostResponseDtos(List<Post> posts, Member member, LocalDateTime now) {
		return posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isAuthor = member.getId().equals(post.getAuthor().getId());
			boolean hasJoined = postjoinRepository.existsByMemberAndPost(member, post);
			boolean isJoined = isAuthor || hasJoined;
			boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
			boolean isEnded = now.isAfter(post.getDueDate());
			boolean isReviewed = scoreRepository.existsByParticipantAndPost(member, post);
			Long joinedMemberCount = postjoinRepository.countByPost(post) + 1;

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded, isAuthor, isReviewed, joinedMemberCount);
		}).collect(Collectors.toList());
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
		boolean isAuthor = true;
		boolean isReviewed = false;
		Long joinedMemberCount = 1L;

		PostResponseDto postResponseDto = PostResponseDto.of(post, imageUrls, Optional.of(isJoined), Optional.of(isHearted), isEnded, isAuthor, isReviewed, joinedMemberCount);

		return ResponseEntity.ok(createStatusResponse(postResponseDto));
	}

	// 플로깅 게시글 상세 보기 : 1개
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPost(Long postId, Member member) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(postId));

		List<String> urlList = postImageRepository.findAllByPost(post)
			.stream()
			.map(PostImage::getFileUrl)
			.collect(Collectors.toList());

		boolean isAuthor = member.getId().equals(post.getAuthor().getId());
		boolean hasJoined = postjoinRepository.existsByMemberAndPost(member, post);
		boolean isJoined = isAuthor || hasJoined;
		boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
		boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());
		boolean isReviewed = scoreRepository.existsByParticipantAndPost(member, post);
		Long joinedMemberCount = postjoinRepository.countByPost(post) + 1;


		PostResponseDto responseDto = PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded, isAuthor, isReviewed, joinedMemberCount);
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
			boolean isReviewed = scoreRepository.existsByParticipantAndPost(member, post);
			Long joinedMemberCount = postjoinRepository.countByPost(post) + 1;

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded, isAuthor, isReviewed, joinedMemberCount);
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
			boolean isReviewed = scoreRepository.existsByParticipantAndPost(member, post);
			Long joinedMemberCount = postjoinRepository.countByPost(post) + 1;

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded, isAuthor, isReviewed, joinedMemberCount);
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
			boolean isReviewed = scoreRepository.existsByParticipantAndPost(member, post);
			Long joinedMemberCount = postjoinRepository.countByPost(post) + 1;

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded, isAuthor, isReviewed, joinedMemberCount);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 플로깅 게시글 삭제
	public ResponseEntity<StatusResponse> deletePost(Member member, Long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostNotFoundException(postId));
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
			.orElseThrow(() -> new MemberNotFoundException());
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
				boolean isReviewed = scoreRepository.existsByParticipantAndPost(member, post);
				Long joinedMemberCount = postjoinRepository.countByPost(post) + 1;

				return PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded, isAuthor, isReviewed, joinedMemberCount);
			}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(hostedPostsDtos));
	}

	// 로그인한 사용자가 주최하거나 참여한 플로깅 게시글을 조회하는 메소드 (단일 리스트로 반환)
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getJoinedPosts(Member member) {
		LocalDateTime now = LocalDateTime.now();

		// 사용자가 주최한 게시글 조회
		List<Post> hostedPosts = postRepository.findAllByAuthor(member, Sort.by(Sort.Direction.DESC, "createdAt"));

		// 사용자가 참여한 게시글 조회
		List<Postjoin> joinedPostjoins = postjoinRepository.findByMemberOrderByPostCreatedAtDesc(member);
		List<Post> joinedPosts = joinedPostjoins.stream()
			.map(Postjoin::getPost)
			.collect(Collectors.toList());

		// 두 리스트를 합치고 중복 제거
		Set<Post> allPosts = new LinkedHashSet<>(hostedPosts);
		allPosts.addAll(joinedPosts);

		List<PostResponseDto> allPostsDtos = allPosts.stream()
			.map(post -> {
				List<String> urlList = postImageRepository.findAllByPost(post)
					.stream()
					.map(PostImage::getFileUrl)
					.collect(Collectors.toList());

				boolean isAuthor = member.getId().equals(post.getAuthor().getId());
				boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
				boolean isEnded = now.isAfter(post.getStartDate());
				boolean isReviewed = scoreRepository.existsByParticipantAndPost(member, post);
				Long joinedMemberCount = postjoinRepository.countByPost(post) + 1;

				return PostResponseDto.of(post, urlList, Optional.of(true),
					Optional.of(isHearted), isEnded, isAuthor, isReviewed, joinedMemberCount);
			})
			.sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))  // 생성일 기준 내림차순 정렬
			.collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(allPostsDtos));
	}

	// 모집 중인 게시글 조회
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getRecruitingPosts(Member member) {
		LocalDateTime now = LocalDateTime.now();
		List<Post> posts = postRepository.findAllByDueDateAfterOrderByCreatedAtDesc(now);
		List<Post> filteredPosts = posts.stream()
			.filter(post -> post.getAuthor().getId().equals(member.getId()) ||
				postjoinRepository.existsByMemberAndPost(member, post))
			.collect(Collectors.toList());
		List<PostResponseDto> responseDtos = createPostResponseDtos(filteredPosts, member, now);
		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 모집 성공한 게시글 조회
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getSuccessfulRecruitmentPosts(Member member) {
		LocalDateTime now = LocalDateTime.now();
		List<Post> posts = postRepository.findAllByDueDateBeforeAndStartDateAfterAndIsRecruitmentSuccessfulTrueOrderByCreatedAtDesc(now, now);
		List<Post> filteredPosts = posts.stream()
			.filter(post -> post.getAuthor().getId().equals(member.getId()) ||
				postjoinRepository.existsByMemberAndPost(member, post))
			.collect(Collectors.toList());
		List<PostResponseDto> responseDtos = createPostResponseDtos(filteredPosts, member, now);
		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 완료된 게시글 조회
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getCompletedPosts(Member member) {
		LocalDateTime now = LocalDateTime.now();
		List<Post> posts = postRepository.findAllByStartDateBeforeAndIsRecruitmentSuccessfulTrueOrderByCreatedAtDesc(now);
		List<Post> filteredPosts = posts.stream()
			.filter(post -> post.getAuthor().getId().equals(member.getId()) ||
				postjoinRepository.existsByMemberAndPost(member, post))
			.collect(Collectors.toList());
		List<PostResponseDto> responseDtos = createPostResponseDtos(filteredPosts, member, now);
		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 가장 최근에 완료한 플로깅 게시글 1개 조회
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getLatestCompletedPost(Member member) {
		LocalDateTime now = LocalDateTime.now();
		List<Post> completedPosts = postRepository.findAllByDueDateBeforeOrderByDueDateDesc(now);
		Optional<Post> latestCompletedPost = completedPosts.stream()
			.filter(post -> post.getAuthor().getId().equals(member.getId()) ||
				postjoinRepository.existsByMemberAndPost(member, post))
			.findFirst();

		if (latestCompletedPost.isPresent()) {
			PostResponseDto responseDto = createPostResponseDto(latestCompletedPost.get(), member, now);
			return ResponseEntity.ok(createStatusResponse(responseDto));
		} else {
			return ResponseEntity.ok(createStatusResponse(null));
		}
	}

	private PostResponseDto createPostResponseDto(Post post, Member member, LocalDateTime now) {
		List<String> urlList = postImageRepository.findAllByPost(post)
			.stream()
			.map(PostImage::getFileUrl)
			.collect(Collectors.toList());

		boolean isAuthor = member.getId().equals(post.getAuthor().getId());
		boolean hasJoined = postjoinRepository.existsByMemberAndPost(member, post);
		boolean isJoined = isAuthor || hasJoined;
		boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
		boolean isEnded = now.isAfter(post.getDueDate());
		boolean isReviewed = scoreRepository.existsByParticipantAndPost(member, post);
		Long joinedMemberCount = postjoinRepository.countByPost(post) + 1;

		return PostResponseDto.of(post, urlList, Optional.of(isJoined), Optional.of(isHearted), isEnded, isAuthor, isReviewed, joinedMemberCount);
	}

	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getFilteredPosts(PostFilterDto filterDto, Member member) {
		Specification<Post> spec = Specification.where(null);

		if (filterDto.getAllGender() != null) {
			spec = spec.and(PostSpecification.withGender(filterDto.getAllGender(), member.getGender()));
		}
		if (filterDto.getWithPet() != null) {
			spec = spec.and(PostSpecification.withPet(filterDto.getWithPet()));
		}
		if (filterDto.getDistricts() != null && !filterDto.getDistricts().isEmpty()) {
			spec = spec.and(PostSpecification.withDistricts(filterDto.getDistricts()));
		}
		if (filterDto.getAllAge() != null) {
			spec = spec.and(PostSpecification.withAgeRange(filterDto.getAllAge(), member.getAge()));
		}
		if (filterDto.getExcludeClosedRecruitment() != null) {
			spec = spec.and(PostSpecification.excludeClosedRecruitment(filterDto.getExcludeClosedRecruitment()));
		}

		List<Post> filteredPosts = postRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));
		List<PostResponseDto> responseDtos = createPostResponseDtos(filteredPosts, member, LocalDateTime.now());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}
}
