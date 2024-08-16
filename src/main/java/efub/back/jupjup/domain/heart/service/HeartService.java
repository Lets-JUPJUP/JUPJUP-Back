package efub.back.jupjup.domain.heart.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import efub.back.jupjup.domain.heart.domain.Heart;
import efub.back.jupjup.domain.heart.dto.HeartResponseDto;
import efub.back.jupjup.domain.heart.exception.DuplicateHeartException;
import efub.back.jupjup.domain.heart.repository.HeartRepository;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.domain.Post;
import efub.back.jupjup.domain.post.domain.PostImage;
import efub.back.jupjup.domain.post.dto.PostResponseDto;
import efub.back.jupjup.domain.post.exception.PostNotFoundException;
import efub.back.jupjup.domain.post.repository.PostImageRepository;
import efub.back.jupjup.domain.post.repository.PostRepository;
import efub.back.jupjup.domain.postjoin.repository.PostjoinRepository;
import efub.back.jupjup.domain.score.repository.ScoreRepository;
import efub.back.jupjup.global.response.StatusEnum;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HeartService {
	private final HeartRepository heartRepository;
	private final PostRepository postRepository;
	private final PostImageRepository postImageRepository;
	private final PostjoinRepository postjoinRepository;
	private final ScoreRepository scoreRepository;

	// 게시글 찜하기
	public ResponseEntity<StatusResponse> heartPost(Member member, Long postId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

		// 이미 좋아요를 눌렀는지 확인
		if (heartRepository.existsByMemberAndPost(member, post)) {
			throw new DuplicateHeartException();
		}

		Heart heart = new Heart(member, post);
		heartRepository.save(heart);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(postId + "번 게시글 찜하였습니다.")
			.build());
	}

	// 게시글 찜하기 취소
	public ResponseEntity<StatusResponse> unheartPost(Member member, Long postId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

		Heart existingHeart = heartRepository.findByMemberAndPost(member, post)
			.orElseThrow(() -> new RuntimeException("참여 정보가 없습니다."));

		heartRepository.delete(existingHeart);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(postId + "번 게시글 찜하기를 취소하였습니다.")
			.build());
	}

	// 찜한 글 모아보기 (수정됨)
	@Transactional(readOnly = true)
	public ResponseEntity<StatusResponse> findHeartPostList(Member member) {
		List<Heart> heartList = heartRepository.findAllByMemberOrderByIdDesc(member);

		List<PostResponseDto> postList = heartList.stream().map(heart -> {
			Post post = postRepository.findById(heart.getPost().getId())
				.orElseThrow(() -> new PostNotFoundException(heart.getPost().getId()));

			List<String> imgUrlList = postImageRepository.findAllByPost(post)
				.stream()
				.map(PostImage::getFileUrl)
				.collect(Collectors.toList());

			boolean isJoined = postjoinRepository.existsByMemberAndPost(member, post);
			boolean isHearted = heartRepository.existsByMemberAndPost(member, post);
			boolean isEnded = LocalDateTime.now().isAfter(post.getDueDate());
			boolean isAuthor = post.getAuthor().getId().equals(member.getId());
			boolean isReviewed = scoreRepository.existsByParticipantAndPost(member, post);
			Long joinedMemberCount = postjoinRepository.countByPost(post);

			return PostResponseDto.of(post, imgUrlList, Optional.of(isJoined), Optional.of(isHearted), isEnded, isAuthor, isReviewed, joinedMemberCount);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(postList)
			.build());
	}
}
