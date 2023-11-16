package efub.back.jupjup.domain.image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import efub.back.jupjup.domain.image.service.ImageService;
import efub.back.jupjup.domain.member.domain.Member;
import efub.back.jupjup.domain.post.dto.ImageUploadRequestDto;
import efub.back.jupjup.domain.post.dto.ImageUploadResponseDto;
import efub.back.jupjup.domain.security.userInfo.AuthUser;
import efub.back.jupjup.global.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {
	private final ImageService imageService;

	@PostMapping
	public ResponseEntity<StatusResponse> getPresignedUrls(
		@AuthUser Member member,
		@RequestBody ImageUploadRequestDto imageUploadRequestDto) {

		List<ImageUploadResponseDto> presignedUrls = imageService.getPresignedUrls(
			imageUploadRequestDto.getImageList());

		StatusResponse statusResponse = imageService.createStatusResponse(presignedUrls);
		return ResponseEntity.ok(statusResponse);
	}
}
