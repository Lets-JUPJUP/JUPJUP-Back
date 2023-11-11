package efub.back.jupjup.domain.post.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import efub.back.jupjup.domain.image.S3Upload;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostAgeRange;
import efub.back.jupjup.domain.post.domain.PostGender;
import efub.back.jupjup.domain.post.domain.PostImage;
import efub.back.jupjup.domain.post.dto.ImageUploadRequestDto;
import efub.back.jupjup.domain.post.dto.ImageUploadResponseDto;
import efub.back.jupjup.domain.post.dto.PostRequestDto;
import efub.back.jupjup.domain.post.dto.PostResponseDto;
import efub.back.jupjup.domain.post.exception.EmptyInputFilenameException;
import efub.back.jupjup.domain.post.exception.WrongImageFormatException;
import efub.back.jupjup.domain.post.repository.PostImageRepository;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.postjoin.repository.PostjoinRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
	private final S3Upload s3Upload;
	private final PostRepository postRepository;
	private final PostImageRepository postImageRepository;
	private final PostjoinRepository postjoinRepository;

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
		postRepository.save(post);

		List<String> imageUrls = saveImageUrls(requestDto.getImages(), post);
		boolean isJoined = false;
		boolean isEnded = false;

		PostResponseDto postResponseDto = PostResponseDto.of(post,imageUrls, Optional.of(isJoined), isEnded);

		return ResponseEntity.ok(createStatusResponse(postResponseDto));
	}

	// 플로깅 게시글 상세 보기 : 1개
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPost(Long postId, Member member){
		Post post = postRepository.findById(postId).orElseThrow();

		List<String> urlList = postImageRepository.findAllByPost(post)
			.stream()
			.map(PostImage::getFileUrl)
			.collect(Collectors.toList());

		boolean isJoined = postjoinRepository.existsByMemberAndPost(member, post);
		boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

		PostResponseDto responseDto = PostResponseDto.of(post, urlList, Optional.of(isJoined), isEnded);
		return ResponseEntity.ok(createStatusResponse(responseDto));
	}

	// 플로깅 게시글 리스트 보기
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getAllPosts(Member member){
		List<Post> posts = postRepository.findAll();

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isJoined = postjoinRepository.existsByMemberAndPost(member, post);
			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 플로깅 게시글 리스트 보기 - (로그인 없이)
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getAllPostsUnAuth(){
		List<Post> posts = postRepository.findAll();

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.empty(), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 성별을 기준으로 게시글 필터링 하는 기능
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByGender(String postGenderStr, Member member) {

		PostGender postGender = PostGender.valueOf(postGenderStr.toUpperCase());
		List<Post> posts = postRepository.findAllByPostGender(postGender);

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isJoined = postjoinRepository.existsByMemberAndPost(member, post);
			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 성별을 기준으로 게시글 필터링 하는 기능 - (로그인 없이)
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByGenderUnAuth(String postGenderStr) {

		PostGender postGender = PostGender.valueOf(postGenderStr.toUpperCase());
		List<Post> posts = postRepository.findAllByPostGender(postGender);

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.empty(), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 나이를 기준으로 게시글 필터링 하는 기능
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByAgeRange(PostAgeRange postAge, Member member) {

		List<Post> posts = postRepository.findAllByPostAgeRangesContaining(postAge);

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isJoined = postjoinRepository.existsByMemberAndPost(member, post);
			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 나이를 기준으로 게시글 필터링 하는 기능 - (로그인 없이)
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByAgeRangeUnAuth(PostAgeRange postAge) {

		List<Post> posts = postRepository.findAllByPostAgeRangesContaining(postAge);

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.empty(), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 반려동물 여부 기준으로 게시글 필터링 하는 기능
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByWithPet(boolean withPetValue, Member member) {

		List<Post> posts = postRepository.findAllByWithPet(withPetValue);

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isJoined = postjoinRepository.existsByMemberAndPost(member, post);
			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.of(isJoined), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 반려동물 여부 기준으로 게시글 필터링 하는 기능 - (로그인 없이)
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPostsByWithPetUnAuth(boolean withPetValue) {

		List<Post> posts = postRepository.findAllByWithPet(withPetValue);

		List<PostResponseDto> responseDtos = posts.stream().map(post -> {
			List<String> urlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());

			return PostResponseDto.of(post, urlList, Optional.empty(), isEnded);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(responseDtos));
	}

	// 플로깅 게시글 삭제
	public ResponseEntity<StatusResponse> deletePost(Member member, Long postId) {
		Post post = postRepository.findById(postId).orElseThrow();
		checkValidMember(member.getId(), post.getAuthor().getId());
		postImageRepository.deleteAllByPost(post);
		postRepository.delete(post);
		return ResponseEntity.ok(createStatusResponse("post deleted"));
	}

	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> getPresignedUrls(Member member, ImageUploadRequestDto imageUploadRequestDto) {
		List<String> fileNameList = imageUploadRequestDto.getImageList();
		log.info(String.valueOf(fileNameList.size()));

		List<ImageUploadResponseDto> urls = fileNameList.stream().map(raw -> {
			//이미지 파일이 유효한 확장자인지 검사
			isValidFileExtension(raw);
			String fileName = createRandomFileName(raw);
			String presignedUrl = s3Upload.getPresignedUrl(fileName);

			return ImageUploadResponseDto.builder()
				.rawFile(raw)
				.fileName(fileName)
				.presignedUrl(presignedUrl)
				.build();
		}).collect(Collectors.toList());

		return ResponseEntity.ok(createStatusResponse(urls));
	}

	private List<String> saveImageUrls(List<String> images, Post post) {
		List<String> imageUrls = new ArrayList<>();
		for (String fileUrl : images) {
			PostImage postImage = postImageRepository.save(new PostImage(fileUrl, post));
			imageUrls.add(postImage.getFileUrl());
		}

		return imageUrls;
	}

	private String createRandomFileName(String rawFile) {
		return UUID.randomUUID() + rawFile;
	}

	private void isValidFileExtension(String fileName) {
		if (fileName.length() == 0) {
			throw new EmptyInputFilenameException();
		}

		String[] validExtensions = {".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG"};
		String idxFileName = fileName.substring(fileName.lastIndexOf("."));
		if (!Arrays.asList(validExtensions).contains(idxFileName)) {
			throw new WrongImageFormatException();
		}
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
}
