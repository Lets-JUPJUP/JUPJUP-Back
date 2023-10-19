package efub.back.jupjup.domain.post.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import efub.back.jupjup.domain.image.S3Upload;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.PostAgeRange;
import efub.back.jupjup.domain.post.domain.PostGender;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostImage;
import efub.back.jupjup.domain.post.dto.ImageUploadRequestDto;
import efub.back.jupjup.domain.post.dto.ImageUploadResponseDto;
import efub.back.jupjup.domain.post.dto.PostRequestDto;
import efub.back.jupjup.domain.post.exception.EmptyInputFilenameException;
import efub.back.jupjup.domain.post.exception.WrongImageFormatException;
import efub.back.jupjup.domain.post.repository.PostImageRepository;
import efub.back.jupjup.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
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

	// 플로깅 게시글 작성
	@Transactional
	public Post addPost(Member member, PostRequestDto requestDto) throws IOException {
		String title = requestDto.getTitle();
		String content = requestDto.getContent();
		String startPlace = requestDto.getStartPlace();
		LocalDateTime startDate = requestDto.getStartDate();
		Integer minMember = requestDto.getMinMember();
		Integer maxMember = requestDto.getMaxMember();
		PostGender postGender = requestDto.getPostGender();
		List<PostAgeRange> postAgeRanges = requestDto.getPostAgeRanges();
		LocalDateTime dueDate = requestDto.getDueDate();
		Long memberId = member.getId();

		Post post = new Post(title, content, startPlace, startDate, minMember, maxMember, postGender, postAgeRanges, dueDate, memberId);
		postRepository.save(post);

		List<String> images = requestDto.getImages();

		if (images != null && !images.isEmpty()) {
			uploadImages(images, post);
		}
		return post;
	}

	private void uploadImages(List<String> images, Post post) {

		// 이미지가 5장 이하인지 확인
		if (images.size() > 5) {
			throw new IllegalArgumentException("이미지는 최대 5장까지만 첨부할 수 있습니다.");
		}

		// 유효한 파일 확장자인지 검사
		images.forEach(this::isValidFileExtension);

		// pre-signed URL을 통해 이미지를 S3에 업로드하고, 해당 이미지의 S3 URL을 반환
		ImageUploadRequestDto imageUploadRequestDto = new ImageUploadRequestDto(images);
		List<ImageUploadResponseDto> presignedUrls = getPresignedUrls(null, imageUploadRequestDto);

		// 클라이언트에게 반환해 줄 수 있는 이미지 업로드를 위한 pre-signed URL 목록
		List<String> s3PresignedUrls = presignedUrls.stream()
			.map(ImageUploadResponseDto::getPresignedUrl)
			.collect(Collectors.toList());

		saveImageUrls(s3PresignedUrls, post);
	}

	// 최신 작성 순으로 정렬된 플로깅 게시글 전체 목록 조회
	public List<Post> findPostList() {
		return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
	}

	// 플로깅 게시글 조회 : 1개 조회
	public Post findPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 플로깅 게시글입니다."));
		return post;
	}

	// 플로깅 게시글 삭제
	public void deletePost(Member member, Long postId) {
		Post post = findPost(postId);
		postRepository.delete(post);
	}

	@Transactional(readOnly = true)
	public List<ImageUploadResponseDto> getPresignedUrls(Member member, ImageUploadRequestDto imageUploadRequestDto) {
		List<String> fileNameList = imageUploadRequestDto.getImageList();

		return fileNameList.stream().map(raw -> {
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
}
